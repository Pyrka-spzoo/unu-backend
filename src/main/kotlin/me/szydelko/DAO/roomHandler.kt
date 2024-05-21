package me.szydelko.DAO

import me.szydelko.companion.Glovo
import me.szydelko.controller.rooms

class RoomHandler(val room: Room,val connectionWS: ConnectionWS) {

    fun leaveTheRoom() {
        room.users -= connectionWS;
    }

    fun kickOutRoom(name: String): Boolean {
        if (room.users.first().name == connectionWS.name) return false
        val connectionWS1 = room.users.find { it.name == name } ?: return false; // @TODO wysukiwanie po id a nie po name bo random bedzie
        room.users -= connectionWS;
        return true
    }

}

fun Room.RoomHandler(connectionWS: ConnectionWS): RoomHandler = RoomHandler(this,connectionWS);
fun ConnectionWS.RoomHandler(): RoomHandler = RoomHandler(Glovo.rooms.getPlayerRoom(this),this);
