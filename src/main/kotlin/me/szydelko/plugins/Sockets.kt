package me.szydelko.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import me.szydelko.DAO.ConnectionWS
import me.szydelko.companion.Glovo
import me.szydelko.controller.generalMessage
import me.szydelko.controller.players
import me.szydelko.controller.roomMessage
import me.szydelko.controller.rooms
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList


fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
//    routing {
//        val connections =  CopyOnWriteArrayList<ConnectionWS>()
//        webSocket("/chat") {
//            println("Adding user!")
//            val thisConnection = ConnectionWS(this)
//            connections += thisConnection
//            try {
//                send("You are connected! There are ${connections.count()} users here.")
//                for (frame in incoming) {
//                    frame as? Frame.Text ?: continue
//                    val receivedText = frame.readText()
//                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
//                    connections.forEach {
//                        it.session.send(textWithUsername)
//                    }
//                }
//            } catch (e: Exception) {
//                println(e.localizedMessage)
//            } finally {
//                println("Removing $thisConnection!")
//                connections -= thisConnection
//            }
//        }
//    }

    routing {
        webSocket("/ws") {
            val thisConnection = ConnectionWS(this)
            Glovo.players.connections += thisConnection
            println(Glovo.players.connections.size);
            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    Glovo.generalMessage(receivedText,thisConnection) ?:
                    Glovo.roomMessage(receivedText,Glovo.rooms.getPlayerRoom(thisConnection))


                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                Glovo.players.connections -= thisConnection
                // @TODO jeśli jest w pokoju musi z nego wyjść
            }
        }
    }
}
