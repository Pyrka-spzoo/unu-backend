package me.szydelko.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin()
{
    val appModule = module {

    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }



}
