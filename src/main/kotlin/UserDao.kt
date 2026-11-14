package com.example

import com.example.database.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException
import kotlin.use

class UserDao {
    suspend fun getUsers(): List<User> =
        withContext(Dispatchers.IO) {

            val users = mutableListOf<User>()

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT id, name, email, phone FROM users"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        while (it.next()) {

                            users.add(
                                User(
                                    id = it.getInt("id"),
                                    name = it.getString("name"),
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

    suspend fun getUser(id: Int): User? =
        withContext(Dispatchers.IO) {

            var user: User? = null

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT id, name, email, phone FROM users WHERE id = $id"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        while (it.next()) {
                            user = User(
                                id = it.getInt("id"),
                                name = it.getString("name"),
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

            try {
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
                                name = it.getString("name"),
                                email = it.getString("email"),
                                phone = it.getString("phone")
                            )
                        }

                    }
                }

            } catch (e: SQLException) {
                println("Error in setUser: $e")
            }

        }
    }
}