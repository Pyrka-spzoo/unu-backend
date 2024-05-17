package me.szydelko.controller

import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DTO.CardItem
import me.szydelko.companion.CardSet
import me.szydelko.companion.Glovo
import java.util.concurrent.CopyOnWriteArrayList


interface RoomController {

    val rooms: MutableList<Room>;

    fun isInRoom(connectionWS: ConnectionWS): Boolean;

    fun getPlayerRoom(connectionWS: ConnectionWS) : Room;

    fun createRoom(connectionWS: ConnectionWS,cardSet: List<CardItem> = CardSet.default): Int

    fun joinToRoom(connectionWS: ConnectionWS,id: Int): Int

    }

val Glovo.Companion.rooms: RoomController by lazy {
    object : RoomController {

        val _rooms: MutableList<Room> = CopyOnWriteArrayList();

        override val rooms: MutableList<Room>
            get() = _rooms;

        override fun isInRoom(connectionWS: ConnectionWS) =
            _rooms.any {
                it.users.any() { c ->
                    c.session == connectionWS.session
                }
            }

        override fun getPlayerRoom(connectionWS: ConnectionWS): Room {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            return _rooms.find { it.users.any { c -> c.session == connectionWS.session } }!!;
        }

        override fun createRoom(connectionWS: ConnectionWS, cardSet: List<CardItem>): Int {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            val element = Room(connectionWS, cardSet)
            _rooms.add(element);
            return element.id;
        }

        override fun joinToRoom(connectionWS: ConnectionWS,id: Int): Int {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            _rooms.find { it.id == id }?.users?.add(connectionWS) ?: throw Exception();
            return id;
        }


    }
}
