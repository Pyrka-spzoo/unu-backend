package me.szydelko.companion

import kotlinx.serialization.Serializable
import me.szydelko.DTO.CardItem

class CardSet {
    companion object {
        val default = listOf(
            CardItem("0","red",1),
            CardItem("1","red",2),
            CardItem("2","red",2),
            CardItem("3","red",2),
            CardItem("4","red",2),
            CardItem("5","red",2),
            CardItem("6","red",2),
            CardItem("7","red",2),
            CardItem("8","red",2),
            CardItem("9","red",2),
            CardItem("stop","red",2),
            CardItem("<=>","red",2),
            CardItem("+2","red",2),

            CardItem("0","green",1),
            CardItem("1","green",2),
            CardItem("2","green",2),
            CardItem("3","green",2),
            CardItem("4","green",2),
            CardItem("5","green",2),
            CardItem("6","green",2),
            CardItem("7","green",2),
            CardItem("8","green",2),
            CardItem("9","green",2),
            CardItem("stop","green",2),
            CardItem("<=>","green",2),
            CardItem("+2","green",2),

            CardItem("0","blue",1),
            CardItem("1","blue",2),
            CardItem("2","blue",2),
            CardItem("3","blue",2),
            CardItem("4","blue",2),
            CardItem("5","blue",2),
            CardItem("6","blue",2),
            CardItem("7","blue",2),
            CardItem("8","blue",2),
            CardItem("9","blue",2),
            CardItem("stop","blue",2),
            CardItem("<=>","blue",2),
            CardItem("+2","blue",2),

            CardItem("0","yellow",1),
            CardItem("1","yellow",2),
            CardItem("2","yellow",2),
            CardItem("3","yellow",2),
            CardItem("4","yellow",2),
            CardItem("5","yellow",2),
            CardItem("6","yellow",2),
            CardItem("7","yellow",2),
            CardItem("8","yellow",2),
            CardItem("9","yellow",2),
            CardItem("stop","yellow",2),
            CardItem("<=>","yellow",2),
            CardItem("+2","yellow",2),

            CardItem("+4","black",4),
            CardItem("color","black",4),
        )
    }
}