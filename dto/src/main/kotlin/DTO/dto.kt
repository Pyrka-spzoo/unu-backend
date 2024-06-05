package me.szydelko.DTO

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import kotlinx.serialization.Serializable


interface Jsonable {
    fun toJson(json: Json = Json): String;
}

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
data class ErrorMessageDTO(val message: String);

@Serializable
data class WrongMessage(val message: String);

@Serializable
sealed class Mess(val message: String) : Jsonable {

    @Serializable
    class PayloadMap : Mess{
        constructor(message: String, payload: MutableMap<String, String>) : super(message) {
        this.payload = payload
        }
        val payload: MutableMap<String,String>;
    }
    @Serializable
    class PayloadJsonElement : Mess{
        constructor(message: String, payload: JsonElement) : super(message) {
            this.payload = payload
        }
        val payload: JsonElement;
    }

    @Serializable
    data class ErrorMessage(val payload: ErrorMessageDTO) : Mess("errorMessage");
    @Serializable
    data class WrongMessage(val payload: WrongMessage) : Mess("wrongMessage");

    @Serializable
    data class MyInfo(val payload: UserDTO) : Mess("myInfo");
    @Serializable
    data class ListRooms(val payload: List<RoomDTO>) : Mess("listRooms");
    @Serializable
    data class ListPlayers(val payload: List<UserDTO>) : Mess("listRooms");




    override fun toJson(json: Json): String = json.encodeToString(json.encodeToJsonElement(this).let {
        return@let JsonObject(it.jsonObject.toMutableMap().filter { i -> i.key!= "type" })
    })
//    override fun toJson(json: Json): String = json.encodeToString(Mess.serializer(),this)
}


@Deprecated("me.szydelko.DTO.Mess.Map")
@Serializable
data class MessageDTO(val message: String,val payload: MutableMap<String,String>);

@Deprecated("me.szydelko.DTO.Mess.PayloadJsonElement")
@Serializable
data class MessageDTO2(val message: String,val payload: JsonElement);

@Deprecated("me.szydelko.DTO.Mess")
infix fun String.payload(payload: MutableMap<String,String>): MessageDTO = MessageDTO(this, payload);
@Deprecated("me.szydelko.DTO.Mess")
inline infix fun <reified R> String.payload(payload: R): MessageDTO2 {
    val serializer: KSerializer<R> = serializer()
    return MessageDTO2(this, Json.encodeToJsonElement(serializer, payload))
}
@Deprecated("me.szydelko.DTO.Mess")
fun MessageDTO.toJson(json: Json = Json): String = json.encodeToString(this);
@Deprecated("me.szydelko.DTO.Mess")
fun MessageDTO2.toJson(json: Json = Json): String = json.encodeToString(this);
@Deprecated("me.szydelko.DTO.Mess")
inline fun <reified M> M.toJson(json: Json = Json): String = json.encodeToString(this);
