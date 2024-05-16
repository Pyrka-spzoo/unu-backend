package me.szydelko.controller

import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DTO.NameDTO
import me.szydelko.companion.Glovo

fun Glovo.Companion.generalMessage(message: String,connectionWS: ConnectionWS) : Boolean{

    val payload = Json.parseToJsonElement(message);
    when(payload.jsonObject["message"]!!.jsonPrimitive.content){
        "rename" -> { connectionWS.name = payload.jsonObject["name"]!!.jsonPrimitive.content }
        "myName" -> {
            runBlocking {  connectionWS.session.send(Json.encodeToString(NameDTO(connectionWS.name))) }
        }
    }
//    {"message":"rename","name":"joo"}
    return false
}

fun Glovo.Companion.roomMessage(message: String,room: Room) : Boolean{



    return false
}