package com.example

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: Int,
    val date: String,
    val subject: String,
    val body: String,
    val status: String,
    val userId: Int
) {
}