package me.szydelko.controller

import me.szydelko.DAO.ConnectionWS
import me.szydelko.companion.Glovo
import java.util.concurrent.CopyOnWriteArrayList

interface ConnectionController {

    val connections : MutableList<ConnectionWS>;


}

val Glovo.Companion.players : ConnectionController
    get() = object : ConnectionController {

        val _conn : MutableList<ConnectionWS> = CopyOnWriteArrayList<ConnectionWS>()

        override val connections: MutableList<ConnectionWS>
            get() = _conn

    }