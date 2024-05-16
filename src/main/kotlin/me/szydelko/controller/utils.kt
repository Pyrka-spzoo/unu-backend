package me.szydelko.controller

import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DTO.MessageDTO
import me.szydelko.DTO.UserDTO
import me.szydelko.companion.Glovo

fun Glovo.Companion.generalMessage(message: String, connectionWS: ConnectionWS): Boolean {

    val payload = Json.parseToJsonElement(message);
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

    }
//    {"message":"rename","name":"joo"}
    return false
}

fun Glovo.Companion.roomMessage(message: String, room: Room): Boolean {


    return false
}