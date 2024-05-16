package me.szydelko.controller

import me.szydelko.DAO.ConnectionWS
import me.szydelko.companion.Glovo
import java.util.concurrent.CopyOnWriteArrayList

interface ConnectionController {

    val connections : MutableList<ConnectionWS>;


}

val Glovo.Companion.players : ConnectionController by lazy {
    object : ConnectionController {

        val _conn : MutableList<ConnectionWS> = CopyOnWriteArrayList()

        override val connections: MutableList<ConnectionWS>
            get() = _conn


    }
}