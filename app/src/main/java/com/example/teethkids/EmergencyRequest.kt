package com.example.teethkids

data class EmergencyRequest(
    val id: String?,
    val title: String?,
    val description: String?,
    val imageUrl1: String?,
    val imageUrl2: String?,
    val imageUrl3: String?,
    val street: String?,
    val streetNumber: String?,
    val city: String?
)