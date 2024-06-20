package me.szydelko

import io.ktor.server.application.*
import me.szydelko.plugins.*
import me.szydelko.utils.Storage


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

interface inu{
   fun i()
}

class ta() : inu{
    override fun i() {
        println("inu")
    }

}

fun Application.module() {
    configureKoin()
    configureSockets()
    configureSerialization()
    configureHTTP()
    configureRouting()
}

