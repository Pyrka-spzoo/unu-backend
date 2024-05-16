package me.szydelko.controller

import me.szydelko.companion.Glovo


interface RoomController {



}

val Glovo.Companion.rooms : RoomController
    get() = object : RoomController {



    }
