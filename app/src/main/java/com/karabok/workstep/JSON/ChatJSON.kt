package com.karabok.workstep.JSON

import com.karabok.workstep.EntityTab.EntityTest
import com.karabok.workstep.JSON.Data.ChatOBJ
import com.karabok.workstep.Loguru.Luna
import org.json.JSONObject

object ChatJSON{
    fun toJson(chat: ChatOBJ): String {
        val jsonObject = JSONObject()
        jsonObject.put("userId", chat.userId)
        jsonObject.put("name", chat.name)
        jsonObject.put("time", chat.time)
        jsonObject.put("message", chat.message)
        jsonObject.put("fileChat", chat.fileChat)
        return jsonObject.toString()
    }

    fun fromJson(jsonString: String): ChatOBJ {
        val jsonObject = JSONObject(jsonString)
        var i: Int = 0
        while (true){
            try {
                i += 1
                jsonObject.getString("$i")

            }
            catch (e: Exception){
                Luna.error(e.message.toString())
            }
        }

        val userId = jsonObject.getInt("userId")
        val name = jsonObject.getString("name")
        val time = jsonObject.getString("time")
        val message = jsonObject.getString("message")
        val fileChat = jsonObject.getString("fileChat")
        return ChatOBJ(userId, name, time, message, fileChat)
    }
}