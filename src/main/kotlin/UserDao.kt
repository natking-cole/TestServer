package com.example



import com.example.database.DatabaseFactory
import com.password4j.Password
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
                    "SELECT id, name, email, phone, passwordHash FROM users"
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
                                    phone = it.getString("phone"),
                                    passwordHash = it.getString("passwordHash")
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
                    "SELECT id, name, email, phone, passwordHash FROM users WHERE id = $id"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        while (it.next()) {
                            user = User(
                                id = it.getInt("id"),
                                name = it.getString("name"),
                                email = it.getString("email"),
                                phone = it.getString("phone"),
                                passwordHash = it.getString("passwordHash")
                            )
                        }

                    }
                }
            }
            user
        }

    suspend fun setUser(userResponse: UserResponse) = withContext(Dispatchers.IO) {
        val hash = Password.hash(userResponse.password).withArgon2().result
        val sql = "INSERT INTO users (name, email, phone, passwordHash) VALUES (?, ?, ?, ?)"

        try {
            DatabaseFactory.getConnection().use { connection ->
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, userResponse.name)
                    statement.setString(2, userResponse.email)
                    statement.setString(3, userResponse.phone)
                    statement.setString(4, hash) //pass
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
                    "SELECT id, name, email, phone, passwordHash FROM users WHERE phone = $phone"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
                            user = User(
                                id = it.getInt("id"),
                                name = it.getString("name"),
                                email = it.getString("email"),
                                phone = it.getString("phone"),
                                passwordHash = it.getString("passwordHash")
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
                    "SELECT id, name, email, phone, passwordHash FROM users WHERE name = $name"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
                            user = User(
                                id = it.getInt("id"),
                                name = it.getString("name"),
                                email = it.getString("email"),
                                phone = it.getString("phone"),
                                passwordHash = it.getString("passwordHash")
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
                    "SELECT id, name, email, phone, passwordHash FROM users WHERE email = $email"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        if (it.next()) {
                            user = User(
                                id = it.getInt("id"),
                                name = it.getString("name"),
                                email = it.getString("email"),
                                phone = it.getString("phone"),
                                passwordHash = it.getString("passwordHash")
                            )
                        }

                    }
                }
            }
            user
        }

    suspend fun getUsersReports(id: Int): List<Report> =
    withContext(Dispatchers.IO) {

        val reports = mutableListOf<Report>()

        DatabaseFactory.getConnection().use { connection ->

            val statement = connection.prepareStatement(
                "SELECT * FROM reports WHERE user_id = $id"
            )

            statement.use { query ->

                val result = query.executeQuery()

                result.use {
                    while (it.next()) {

                        reports.add(
                            Report(
                                id = it.getInt("id"),
                                date = it.getString("date"),
                                subject = it.getString("subject"),
                                status = it.getString("status"),
                                body = it.getString("body"),
                                userId = it.getInt("user_id"),
                            )
                        )
                    }
                }
            }
        }

        reports
    }

    suspend fun getUsersContacts(id: Int): List<Contact> =
        withContext(Dispatchers.IO) {

            val contacts = mutableListOf<Contact>()

            DatabaseFactory.getConnection().use { connection ->

                val statement = connection.prepareStatement(
                    "SELECT * FROM emergency_contacts WHERE user_id = $id"
                )

                statement.use { query ->

                    val result = query.executeQuery()

                    result.use {
                        while (it.next()) {

                            contacts.add(
                                Contact(
                                    entry_id = it.getInt("entry_id"),
                                    user_id = it.getInt("user_id"),
                                    number = it.getString("number"),
                                    name = it.getString("name"),
                                    trusted_id = it.getInt("trusted_id")
                                )
                            )
                        }
                    }
                }
            }

            contacts
        }
}