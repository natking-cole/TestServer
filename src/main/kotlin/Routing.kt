package com.example

import com.example.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.use

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
        get("/users"){
            val users = getUsers()
            call.respond(users)
        }
        get("/users/{id}"){
            val userId = call.parameters["id"]!!.toInt()
            val user = User(
                id = userId,
                name = "hard",
                phone = "421-xxx-xxxx",
                email = "hardcoded@yahoo.com"

            )

            call.respond(user)
        }
    }
}

//temp
private suspend fun getUsers(): List<User> =
    withContext(Dispatchers.IO) {

        val users = mutableListOf<User>()

        DatabaseFactory.getConnection().use { connection ->

            val statement = connection.prepareStatement(
                "SELECT id, username, email, phone FROM users"
            )

            statement.use { query ->

                val result = query.executeQuery()

                result.use {
                    while (it.next()) {

                        users.add(
                            User(
                                id = it.getInt("id"),
                                name = it.getString("username"),
                                email = it.getString("email"),
                                phone = it.getString("phone")
                            )
                        )
                    }
                }
            }
        }

        users
    }