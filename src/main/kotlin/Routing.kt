package com.example

import com.example.database.DatabaseFactory
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.String
import kotlin.use

fun Application.configureRouting() {
    val userDao = UserDao()
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }

            get("/users"){
                val name = call.request.queryParameters["name"]
                val email = call.request.queryParameters["email"]
                val phone = call.request.queryParameters["phone"]
                if(name != null){
                    val user = userDao.getUserByName(name)
                    if(user != null){
                        call.respond(user)
                    }
                    else{
                        call.respond("User not found!")
                    }
                }
                if(email != null){
                    val user = userDao.getUserByEmail(email)
                    if(user != null){
                        call.respond(user)
                    }
                    else{
                        call.respond("User not found!")
                    }
                }
                if(phone != null){
                    val user = userDao.getUserByPhone(phone)
                    if(user != null){
                        call.respond(user)
                    }
                    else{
                        call.respond("User not found!")
                    }
                }
                else{
                    val users = userDao.getUsers()
                    call.respond(users)
                }

            }
        post("/users") {
            try {
                val userResponse = call.receive<UserResponse>()

                println("Received user: $userResponse")

                val user = UserResponse(
                    name = userResponse.name,
                    email = userResponse.email,
                    phone = userResponse.phone,
                    password = userResponse.password,
                )

                userDao.setUser(user)

                call.respond(
                    status = HttpStatusCode.Created,
                    message = userResponse
                )
            } catch (e: Exception) {
                e.printStackTrace()

                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf(
                        "error" to (e.message ?: "Unknown error")
                    )
                )
            }
        }

        get("/users/{id}"){
            val user = userDao.getUser(call.parameters["id"]!!.toInt())
            if (user != null){
                call.respond(user)
            }
            else{
                call.respond("User not found!")
            }

        }
        get("/reports/{id}"){
            val id = call.parameters["id"]!!.toInt()
            val userReports = userDao.getUsersReports(id)
            call.respond(userReports)
        }
        get("/contacts/{id}"){
            val id = call.parameters["id"]!!.toInt()
            val userReports = userDao.getUsersContacts(id)
            call.respond(userReports)
        }

    }
}

