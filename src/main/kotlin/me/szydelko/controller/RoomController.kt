package me.szydelko.controller

import me.szydelko.DAO.ConnectionWS
import me.szydelko.DAO.Room
import me.szydelko.DAO.RoomHandler
import me.szydelko.DTO.CardItem
import me.szydelko.DTO.count
import me.szydelko.DTO.symbol
import me.szydelko.companion.CardSet
import me.szydelko.companion.Glovo
import java.util.concurrent.CopyOnWriteArrayList


interface RoomController {

    val rooms: MutableList<Room>;

    fun isInRoom(connectionWS: ConnectionWS): Boolean;

    fun getPlayerRoom(connectionWS: ConnectionWS) : Room;

    fun createRoom(connectionWS: ConnectionWS,cardSet: List<CardItem> = CardSet.default): Int

    fun joinToRoom(connectionWS: ConnectionWS,id: Int): Int

    fun leaveTheRoom(connectionWS: ConnectionWS)

    fun kickOutRoom(connectionWS: ConnectionWS,name: String): Boolean

    }

val Glovo.Companion.rooms: RoomController by lazy {
    object : RoomController {

        val _rooms: MutableList<Room> = CopyOnWriteArrayList();

        override val rooms: MutableList<Room>
            get() = _rooms;

        override fun isInRoom(connectionWS: ConnectionWS) =
            _rooms.any {
                it.players.any() { c ->
                    c.session == connectionWS.session
                }
            }

        override fun getPlayerRoom(connectionWS: ConnectionWS): Room {
            if (!isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            return _rooms.find { it.players.any { c -> c.session == connectionWS.session } }!!;
        }

        override fun createRoom(connectionWS: ConnectionWS, cardSet: List<CardItem>): Int {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            val element = Room(connectionWS, cardSet)
            _rooms.add(element);
            connectionWS.cards.clear()
            val roomHandler = connectionWS.RoomHandler()
            for (n in 1..7)
                roomHandler.getCard();
            return element.id;
        }

        override fun joinToRoom(connectionWS: ConnectionWS,id: Int): Int {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            connectionWS.cards.clear()
            _rooms.find { it.id == id }?.players?.add(connectionWS) ?: throw Exception();
            val roomHandler = connectionWS.RoomHandler()
            for (n in 1..7)
                roomHandler.getCard(true);
            return id;
        }

        override fun leaveTheRoom(connectionWS: ConnectionWS) {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            getPlayerRoom(connectionWS).players -= connectionWS;
        }

        override fun kickOutRoom(connectionWS: ConnectionWS,name: String): Boolean {
            if (isInRoom(connectionWS)) throw Exception(); // @TODO dodać normalne wyjątki
            val playerRoom = getPlayerRoom(connectionWS)
            if (playerRoom.players.first().name == connectionWS.name) return false
            val connectionWS1 = playerRoom.players.find { it.name == name } ?: return false; // @TODO wysukiwanie po id a nie po name bo random bedzie
            getPlayerRoom(connectionWS).players -= connectionWS1;
            return true
        }


    }
}
