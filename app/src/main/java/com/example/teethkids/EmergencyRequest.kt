package com.example.teethkids

//Classe que cuida dos dados entre o BD e o app
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