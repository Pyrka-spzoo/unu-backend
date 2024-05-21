package me.szydelko.DAO

import me.szydelko.DTO.Card
import me.szydelko.DTO.CardItem
import me.szydelko.DTO.getCard
import me.szydelko.companion.Glovo
import me.szydelko.controller.rooms
import kotlin.random.Random

class RoomHandler(val room: Room,val connectionWS: ConnectionWS) {

    fun leaveTheRoom() {
        room.players -= connectionWS;
    }

    fun kickOutRoom(name: String): Boolean {
        if (room.players.first().name == connectionWS.name) return false
        val connectionWS1 = room.players.find { it.name == name } ?: return false; // @TODO wysukiwanie po id a nie po name bo random bedzie
        room.players -= connectionWS1;
        return true
    }

    fun getCard(): Card {
        var newCard : Card;
        var nextInt = Random.nextInt(0, room.cardSet.size)
        val tmp = nextInt
        do {
            nextInt = (nextInt + 1).takeIf { it < room.cardSet.size } ?: 0
            newCard = room.cardSet[nextInt].getCard()
        }while (room.ifCardIsInDeck(newCard) && tmp != nextInt)

        connectionWS.cards.find { it.symbol == newCard.symbol && it.color == newCard.color }
            ?.let { it.count++ } ?: connectionWS.cards.add(CardItem(newCard.symbol, newCard.color, 1))

        return newCard;
    }

//    fun putCard(card: Card): Card{
//
//    }

}

fun Room.RoomHandler(connectionWS: ConnectionWS): RoomHandler = RoomHandler(this,connectionWS);
fun ConnectionWS.RoomHandler(): RoomHandler = RoomHandler(Glovo.rooms.getPlayerRoom(this),this);
