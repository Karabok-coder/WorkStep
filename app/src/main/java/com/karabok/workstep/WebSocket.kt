package com.karabok.workstep

import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.Utils.Rand
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object WebSocket {
    private val client = OkHttpClient()
    private val rand = Rand()

    private val request = Request.Builder()
        .url("ws://${ConstAPI.domainLink}/ws/${rand.nextInt(100000)}")
        .build()

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            // WebSocket подключение открыто
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Luna.log(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            // Получены байты
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Luna.log("Подключение разорвано")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            // Произошла ошибка при подключении
        }
    }

    val webSocket = client.newWebSocket(request, listener)
}