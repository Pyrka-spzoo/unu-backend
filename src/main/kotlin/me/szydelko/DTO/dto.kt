package me.szydelko.DTO

import kotlinx.serialization.Serializable

@Serializable
data class Card(val symbol: String,val color: String)

@Serializable
data class CardItem(val symbol: String,val color: String,val limit : Int);

@Serializable
data class NameDTO(val name: String);
