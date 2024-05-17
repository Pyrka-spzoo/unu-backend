package me.szydelko.DTO

import kotlinx.serialization.Serializable

@Serializable
data class Card(val symbol: String,val color: String)

@Serializable
data class CardItem(val symbol: String,val color: String,val limit : Int);

@Serializable
data class UserDTO(val name: String,val isInRoom: Boolean);

@Serializable
data class RoomDTO(val id: Int,val users: MutableList<String>);

@Serializable
data class MessageDTO(val message: String,val payload: MutableMap<String,String>);