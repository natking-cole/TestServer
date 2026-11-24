package com.example

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val entry_id: Int,
    val user_id: Int,
    val number: String,
    val name: String,
    val trusted_id: Int
) {
}