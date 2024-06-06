package me.szydelko.controller

import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DAO.RoomHandler
import me.szydelko.companion.Glovo
import me.szydelko.DTO.*

interface Sendable {
   suspend fun send();
}

sealed class MessSendable() : Sendable {

    data class SendToEveryone(val toJson: ToJson) : MessSendable()

    data class SendToOne(val toJson: ToJson, val connectionWS: ConnectionWS) : MessSendable()

    data class SendToMultiple(val toJson: ToJson, val listConnectionWS: List<ConnectionWS>) : MessSendable()

    data class SendToRoom(val toJson: ToJson, val room: Room) : MessSendable()

    data class SendToRoomFromConn(val toJson: ToJson, val connectionWS: ConnectionWS) : MessSendable()


    override suspend fun send() {
         when (this) {
            is SendToEveryone -> {
                Glovo.players.connections.forEach { it.session.send(toJson.toJson()) }
            }
            is SendToOne -> { connectionWS.session.send(toJson.toJson()) }
            is SendToMultiple -> { listConnectionWS.forEach { it.session.send(toJson.toJson()) } }
            is SendToRoom -> { room.players.forEach { it.session.send(toJson.toJson()) } }
            is SendToRoomFromConn -> { connectionWS.RoomHandler().room.players.forEach { it.session.send(toJson.toJson()) } }
        }
    }
}

data class ListSendable(val listSendable: List<Sendable>) : Sendable{
    override suspend fun send() {
        listSendable.forEach{it.send()}
    }
}

fun List<Sendable>.toSendable() : ListSendable = ListSendable(this);


fun Glovo.Companion.generalMessage(message: String, connectionWS: ConnectionWS): Boolean {

    val payload = Json.parseToJsonElement(message); // @TODO sprawdzać czy napewno jest message w json
    when (payload.jsonObject["message"]!!.jsonPrimitive.content) {
        "rename" -> {
            connectionWS.name = payload.jsonObject["name"]!!.jsonPrimitive.content
            return true
        }

        "myInfo" -> {
            runBlocking {
//                connectionWS.session.send(Json.encodeToString(MessageDTO2("myInfo",Json.encodeToJsonElement(UserDTO(connectionWS.name, rooms.isInRoom(connectionWS))))))
//                connectionWS.session.send(Json.encodeToString("myInfo" payload UserDTO(connectionWS.name, rooms.isInRoom(connectionWS))))
//                connectionWS.session.send(("myInfo" payload UserDTO(connectionWS.name, rooms.isInRoom(connectionWS))).toJson())
                connectionWS.sendJsonable(Mess.MyInfo(UserDTO(connectionWS.name, rooms.isInRoom(connectionWS))))
            }
            return true
        }

        "listRooms" -> {
            runBlocking {
//                connectionWS.session.send(Json.encodeToString( "listRoom" payload Glovo.rooms.rooms.map { RoomDTO(it.id,it.players.map { it.name }.toMutableList()) }))
                connectionWS.sendJsonable(Mess.ListRooms(Glovo.rooms.rooms.map { RoomDTO(it.id,it.players.map { it.name }.toMutableList()) }))
            }
            return true
        }

        "listPlayers" -> {
            runBlocking {
                connectionWS.session.send(Json.encodeToString(  "listPlayers" payload Glovo.players.connections.map { UserDTO(it.name,rooms.isInRoom(it)) }))
            }
            return true
        }

        "createRoom" -> {
            val idRoom = rooms.createRoom(connectionWS)
            runBlocking {
                connectionWS.session.send(Json.encodeToString(MessageDTO("createRoom", mutableMapOf("id" to idRoom.toString()))))
                players.connections.forEach() {// @TODO to do funkcji
                    it.session.send(Json.encodeToString(MessageDTO("newRoom", mutableMapOf("id" to idRoom.toString()))))
                }
            }
            return true
        }

        "joinToRoom" -> {
            val id = payload.jsonObject["id"]?.jsonPrimitive?.intOrNull ?: return false
            try {
                Glovo.rooms.joinToRoom(connectionWS,id)
            }catch (e:Exception){ // @TODO rozrurzniać błedy
                runBlocking {
                    connectionWS.session.send(Json.encodeToString(MessageDTO("error", mutableMapOf())))
                }
                throw e;
            }
            runBlocking {
                Glovo.rooms.getPlayerRoom(connectionWS).players.forEach() {
                    it.session.send(Json.encodeToString(MessageDTO("join", mutableMapOf("name" to connectionWS.name))))
                } // @TODO zrobić z tego asychroniczne tak rzeby sie wykonywało
            }
            return true
        }

//        "leaveTheRoom" -> {
//            Glovo.rooms.leaveTheRoom(connectionWS);
//        }
//
//        "kickOutRoom" -> {
//            val name = payload.jsonObject["name"]?.jsonPrimitive?.contentOrNull ?: return false
//            return Glovo.rooms.kickOutRoom(connectionWS,name);
//        }

    }
//    {"message":"rename","name":"joo"}
    return false
}

fun Glovo.Companion.roomMessage(message: String, connectionWS: ConnectionWS): Boolean {

    val roomHandler = connectionWS.RoomHandler() // @TODO w konstruktorze handlera powinno sie badać czy wszystko dobrze

    val payload = Json.parseToJsonElement(message); // @TODO sprawdzać czy napewno jest message w json
    when (payload.jsonObject["message"]!!.jsonPrimitive.content) {

        "leaveTheRoom" -> {
            roomHandler.leaveTheRoom()
            return true
        }

        "kickOutRoom" -> {
            val name = payload.jsonObject["name"]?.jsonPrimitive?.contentOrNull ?: return false
            return roomHandler.kickOutRoom(name);
        }

        "getCard" -> {
            val card = roomHandler.getCard();
            runBlocking {
                connectionWS.session.send(Json.encodeToString(card))
            }
            return true
        }

        "getMyCards" -> {
            runBlocking {
                connectionWS.session.send(Json.encodeToString(connectionWS.cards))
            }
            return true
        }

        "putCard" -> {

            val json = payload.jsonObject["card"]?.jsonObject ?: throw Exception() // @TODO dać jakiś exc związany z strupkurą
            val card = Json.decodeFromJsonElement<Card>(json)
            roomHandler.putCard(card,payload)
            return true
        }


    }


    return false
}