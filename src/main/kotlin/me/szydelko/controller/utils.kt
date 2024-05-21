package me.szydelko.controller

import io.ktor.websocket.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DTO.MessageDTO
import me.szydelko.DTO.RoomDTO
import me.szydelko.DTO.UserDTO
import me.szydelko.companion.Glovo

fun Glovo.Companion.generalMessage(message: String, connectionWS: ConnectionWS): Boolean {

    val payload = Json.parseToJsonElement(message); // @TODO sprawdzać czy napewno jest message w json
    when (payload.jsonObject["message"]!!.jsonPrimitive.content) {
        "rename" -> {
            connectionWS.name = payload.jsonObject["name"]!!.jsonPrimitive.content
            return true
        }

        "myInfo" -> {
            runBlocking {
                connectionWS.session.send(Json.encodeToString(UserDTO(connectionWS.name, rooms.isInRoom(connectionWS))))
            }
            return true
        }

        "listRooms" -> {
            runBlocking {
                connectionWS.session.send(Json.encodeToString(Glovo.rooms.rooms.map { RoomDTO(it.id,it.users.map { it.name }.toMutableList()) }))
            }
            return true
        }

        "listUsers" -> {
            runBlocking {
                connectionWS.session.send(Json.encodeToString(Glovo.players.connections.map { UserDTO(it.name,rooms.isInRoom(it)) }))
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
                Glovo.rooms.getPlayerRoom(connectionWS).users.forEach() {
                    it.session.send(Json.encodeToString(MessageDTO("join", mutableMapOf("name" to connectionWS.name))))
                } // @TODO zrobić z tego asychroniczne tak rzeby sie wykonywało
            }
            return true
        }

        "leaveTheRoom" -> {

        }

        "kickOutRoom" -> {
            val name = payload.jsonObject["name"]?.jsonPrimitive?.intOrNull ?: return false


        }

    }
//    {"message":"rename","name":"joo"}
    return false
}

fun Glovo.Companion.roomMessage(message: String, room: Room): Boolean {


    return false
}