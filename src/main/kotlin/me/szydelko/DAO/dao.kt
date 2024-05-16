package me.szydelko.DAO

import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import me.szydelko.DAO.ConnectionWS.Companion
import me.szydelko.DTO.Card
import me.szydelko.DTO.CardItem
import me.szydelko.companion.CardSet
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class ConnectionWS(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val id = lastId.getAndIncrement()
    var name = "user${id}"
}

class Room(owner: String, val cardSet: List<CardItem> = CardSet.default) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val id = ConnectionWS.lastId.getAndIncrement()
    val users = mutableListOf(owner)
    var direction = true
    var lastCard = cardSet[Random.nextInt(0,cardSet.size)].let { Card(it.symbol,it.color) }
    val cardList = mutableMapOf(lastCard to 1);

    fun ifCardIsInDeck(card: Card) : Boolean{
        return ((cardList.filterKeys { it == card }.values.first()) < (cardSet.find { it.let { Card(it.symbol,it.color) } == card }!!.limit))
    }

    fun getNextCard(): Card{
        var tmp :Card;
        do {
            tmp = cardSet[Random.nextInt(0,cardSet.size)].let { Card(it.symbol,it.color) }
        }while (ifCardIsInDeck(tmp))

        cardList[tmp] = cardList.getOrPut(tmp) { 0 } + 1

        return tmp;
    }
}

