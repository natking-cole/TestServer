package com.example

import com.example.database.DatabaseFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
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
        post("/users") {
            try{
                val user = call.receive<User>()
            }
            catch(e: Exception){
                call.respond(HttpStatusCode.BadRequest)
            }
        }




        get("/users/{id}"){
            val user = getUser(call.parameters["id"]!!.toInt())
            if (user != null){
                call.respond(user)
            }
            else{
                call.respond("User not found!")
            }

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

private suspend fun getUser(id: Int): User? =
    withContext(Dispatchers.IO) {

        var user: User? = null

        DatabaseFactory.getConnection().use { connection ->

            val statement = connection.prepareStatement(
                "SELECT id, username, email, phone FROM users WHERE id = $id"
            )

            statement.use { query ->

                val result = query.executeQuery()

                result.use {
                    while (it.next()) {
                        user = User(
                            id = it.getInt("id"),
                            name = it.getString("username"),
                            email = it.getString("email"),
                            phone = it.getString("phone")
                        )
                    }

                }
            }
        }
        user
    }

private suspend fun setUser(name: String, email: String, phone: String) = withContext(Dispatchers.IO) {
    DatabaseFactory.getConnection().use { connection ->

        val statement = connection.prepareStatement(
            "INSERT INTO users (name, email, phone)" +
                    "VALUES($name, $email, $phone)"
        )

        statement.use { query ->

            val result = query.executeQuery()

            result.use {
                while (it.next()) {
                    val user = User(
                        id = it.getInt("id"),
                        name = it.getString("username"),
                        email = it.getString("email"),
                        phone = it.getString("phone")
                    )
                }

            }
        }
    }

}