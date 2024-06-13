package me.szydelko

import io.ktor.server.application.*
import me.szydelko.plugins.*
import me.szydelko.utils.Storage


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val storage = Storage()
        .push("tak")
        .push("nie","nie")
        .push("tak2")
        .push(3)

    val tak : String = storage.get() ?: "null"
    val nie : String = storage.get("nie") ?: "null"
    val int : Int = storage.get() ?: 0

    println("tak: $tak, ne: $nie , int $int")

    configureKoin()
    configureSockets()
    configureSerialization()
    configureHTTP()
    configureRouting()
}

