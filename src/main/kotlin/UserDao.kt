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

    suspend fun setUser(userResponse: UserResponse) = withContext(Dispatchers.IO) {
        val sql = "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)"

        try {
            DatabaseFactory.getConnection().use { connection ->
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, userResponse.name)
                    statement.setString(2, userResponse.email)
                    statement.setString(3, userResponse.phone)
                    statement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            println("Error in setUser: ${e.message}")
        }
//just commenting so it lets me commit
    }

    suspend fun getUserByPhone(phone: String): User? =
        withContext(Dispatchers.IO) {

            var user: User? = null

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT id, name, email, phone FROM users WHERE phone = $phone"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
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
    suspend fun getUserByName(name: String): User? =
        withContext(Dispatchers.IO) {

            var user: User? = null

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT id, name, email, phone FROM users WHERE name = $name"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
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
    suspend fun getUserByEmail(email: String): User? =
        withContext(Dispatchers.IO) {

            var user: User? = null

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT id, name, email, phone FROM users WHERE email = $email"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
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
}