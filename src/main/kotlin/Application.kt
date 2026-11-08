package com.example

import com.example.database.DatabaseFactory
import io.ktor.server.application.Application

fun Application.rootModule() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()
}
