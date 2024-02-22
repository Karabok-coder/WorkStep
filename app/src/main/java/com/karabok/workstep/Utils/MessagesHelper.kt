package com.karabok.workstep.Utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.EntityTab.EntityMessage
import com.karabok.workstep.File.Groovy


object MessagesHelper {
    public suspend fun getMessages(context: Context, userId: Int): MutableList<EntityMessage>{
        val data: MutableList<EntityMessage> = mutableListOf()
        val nameJson: String = "${ConstApp.fileChat}$userId.json"
        val groovy: Groovy = Groovy(context)
        val messagesJson: String = groovy.read(nameJson)

        if(messagesJson == ""){
            groovy.write("", nameJson)
            return data
        }

        val jsonObject = JsonParser.parseString(messagesJson).asJsonObject

        for ((key, _) in jsonObject.entrySet()) {
            val message = jsonObject.get(key).asJsonObject

            val entityMessage = EntityMessage(
                message.get("id").asInt,
                message.get("sender").asInt,
                message.get("message").asString,
                message.get("date").asString,
            )

            data.add(entityMessage)
        }

        return data
    }

    public suspend fun setMessages(context: Context, userId: Int, message: EntityMessage) {
        val mainJson = JsonObject()
        val dataMessages: MutableList<EntityMessage> = getMessages(context, userId)
        dataMessages.add(message)

        val groovy: Groovy = Groovy(context)
        val nameJson: String = "${ConstApp.fileChat}$userId.json"

        for(values in dataMessages){
            val tempJson = JsonObject()
            tempJson.addProperty("id", values.id)
            tempJson.addProperty("sender", values.sender)
            tempJson.addProperty("message", values.messageText)
            tempJson.addProperty("date", values.messageDate)

            mainJson.add(values.id.toString(), tempJson)
        }

        val gson = Gson()
        val data = gson.toJson(mainJson)
        groovy.write(data, nameJson)
    }
}