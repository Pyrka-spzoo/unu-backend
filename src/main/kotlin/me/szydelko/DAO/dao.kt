package me.szydelko.DAO

import io.ktor.websocket.*
import me.szydelko.DTO.Card
import me.szydelko.DTO.CardItem
import me.szydelko.DTO.toCard
import me.szydelko.companion.CardSet
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class ConnectionWS(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val id = lastId.getAndIncrement()
    var name = "user${id}"
    val cards: MutableList<CardItem> = mutableListOf()
}


class Room(owner: ConnectionWS, val cardSet: List<CardItem> = CardSet.default) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val id = Room.lastId.getAndIncrement()
    val players = mutableListOf(owner)
    var direction = true
    var turnIndex = 0; // @TODO Bazpiecznie usówać graczy z przesówaniem indexów
    var lastCard = cardSet[Random.nextInt(0,cardSet.size)].toCard()
    val cardList = mutableMapOf(lastCard to 1);

    fun ifCardIsInDeck(card: Card) : Boolean{
        return ((cardList.filterKeys { it == card }.values.first()) < (cardSet.find { it.let { Card(it.symbol,it.color) } == card }!!.count))
    }

//    fun getNextCard(): Card{
//        var tmp :Card;
//        do {
//            tmp = cardSet[Random.nextInt(0,cardSet.size)].let { Card(it.symbol,it.color) }
//        }while (ifCardIsInDeck(tmp))
//
//        cardList[tmp] = cardList.getOrPut(tmp) { 0 } + 1
//
//        return tmp;
//    }

}



