package com.example.database
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv

object DatabaseFactory {

    private lateinit var dataSource: HikariDataSource

    fun init() {

        val config = HikariConfig().apply {

            jdbcUrl = dotenv().get("DB_URL")
            username = dotenv().get("DB_USERNAME")
            password = dotenv().get("DB_PASSWORD")

            driverClassName = "com.mysql.cj.jdbc.Driver"

            maximumPoolSize = 5
        }

        dataSource = HikariDataSource(config)
    }

    fun getConnection() = dataSource.connection

    fun close() {
        if (::dataSource.isInitialized) {
            dataSource.close()
        }
    }
}