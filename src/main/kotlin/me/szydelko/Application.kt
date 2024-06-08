package me.szydelko

import io.ktor.server.application.*
import me.szydelko.plugins.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {



    configureKoin()
    configureSockets()
    configureSerialization()
    configureHTTP()
    configureRouting()
}

