package com.example

import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val date: String,
    val subject: String,
    val body: String,
    val userId: Int,
    val status: String
) {
}