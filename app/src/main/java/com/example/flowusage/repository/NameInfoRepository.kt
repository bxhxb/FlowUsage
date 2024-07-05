package com.example.flowusage.repository

class NameInfoRepository {
    var name: String = "init"

    fun changeName(): String {
        name = "changed"
        return name
    }

    fun getNewHobby(): String {
        val randomInteger = (1..5).shuffled().first()
        return "the hobby is " + when (randomInteger) {
            1 -> "music"
            2 -> "sports"
            3 -> "fishing"
            4 -> "video game"
            else -> "watching TV"
        }
    }
}