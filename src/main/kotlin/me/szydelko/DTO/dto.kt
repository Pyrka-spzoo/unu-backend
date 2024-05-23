package me.szydelko.DTO

import kotlinx.serialization.Serializable

@Serializable
data class Card(val symbol: String,val color: String)

infix fun String.symbol(symbol: String): Card = Card(symbol,this)

@Serializable
data class CardItem(val symbol: String,var color: String,var count : Int);

infix fun Card.count(count : Int): CardItem = CardItem(this.symbol,this.color,count)
fun CardItem.toCard() = Card(this.symbol,this.color)

@Serializable
data class UserDTO(val name: String,val isInRoom: Boolean);

@Serializable
data class RoomDTO(val id: Int,val users: MutableList<String>);

@Serializable
data class MessageDTO(val message: String,val payload: MutableMap<String,String>);