package me.szydelko.DAO

import kotlinx.serialization.json.*
import me.szydelko.DTO.Card
import me.szydelko.DTO.CardItem
import me.szydelko.DTO.toCard
import me.szydelko.companion.Glovo
import me.szydelko.controller.rooms
import kotlin.random.Random

class RoomHandler(val room: Room,val connectionWS: ConnectionWS) {

    fun nextPlayer(hopBy:Int = 1): Int{

        (hopBy >= 0) ?: throw Exception() // @TODO lepiej zaimplementować

        var hop = hopBy
        if (hop >= room.players.size){
            hop %= room.players.size;
        }
//
//        room.turnIndex
//        room.direction

        val newTurn = room.turnIndex + hop
        val oldTurn = room.turnIndex - hop // :)
        if ( room.direction || newTurn >= room.players.size){
            hop -= newTurn
            return hop
        }else if (room.direction){
            return room.turnIndex + hop
        }else if (oldTurn <= 0){
            return room.players.size - (hop - room.turnIndex)
        }else {
            return room.turnIndex - hop
        }

    }

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
            newCard = room.cardSet[nextInt].toCard()
        }while (room.ifCardIsInDeck(newCard) && tmp != nextInt)

        room.cardSet.find { it.toCard() == newCard }
            ?.let { it.count++ } ?: {
                connectionWS.cards.add(CardItem(newCard.symbol, newCard.color, 1))
                room.cardList[newCard] = room.cardList.getOrPut(newCard) { 0 } + 1
            }

        return newCard;
    }

    fun putCard(card: Card,payload: JsonElement): Card{

        val item = connectionWS.cards.find { it.toCard() == card } ?: throw Exception() // @TODO znowu uwjątki

        if(item.color == "black" ){

            if(item.symbol == "color"){
                val color = payload.jsonObject["changedColor"]?.jsonPrimitive?.contentOrNull ?: throw Exception() // @TODO shemat
                item.color = color
                room.lastCard = item.toCard()
                return item.toCard();
            }

            if(item.symbol == "+4"){
                val color = payload.jsonObject["changedColor"]?.jsonPrimitive?.contentOrNull ?: throw Exception() // @TODO shemat
                item.color = color
                for (n in 1..4)
                room.players[nextPlayer()].RoomHandler().getCard()
                room.lastCard = item.toCard()
                return item.toCard();
            }

        } else if (item.color == room.lastCard.color || item.symbol == room.lastCard.symbol){



        }



    }

}

fun Room.RoomHandler(connectionWS: ConnectionWS): RoomHandler = RoomHandler(this,connectionWS);
fun ConnectionWS.RoomHandler(): RoomHandler = RoomHandler(Glovo.rooms.getPlayerRoom(this),this);
