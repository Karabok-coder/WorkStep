package com.karabok.workstep.JSON.Data


data class ChatOBJ(
    val userId: Int,
    val name: String,
    val time: String,
    val message: String,
    val fileChat: String
)

data class ChatData(
    var chats: MutableMap<Int, ChatOBJ> = mutableMapOf()
)