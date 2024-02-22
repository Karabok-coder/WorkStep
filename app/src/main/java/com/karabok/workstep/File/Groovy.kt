package com.karabok.workstep.File

import android.content.Context
import com.karabok.workstep.Loguru.Luna
import java.io.BufferedReader
import java.io.InputStreamReader

class Groovy(private val context: Context) {
    fun write(data: String, fileName: String) {
        try {
            val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            Luna.error("Error saving file: ${e.message}")
        }
    }

    fun read(fileName: String): String {
        var result = ""
        try {
            val fileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }

            result = stringBuilder.toString()
            fileInputStream.close()
        } catch (e: Exception) {
            Luna.error("Error reading file: ${e.message}")
        }

        return result
    }

    fun appendStart(data: String, fileName: String){
        val temp: String = read(fileName)
        write("$data\n$temp", fileName)
    }

    fun appendEnd(data: String, fileName: String){
        val temp: String = read(fileName)
        write("$temp\n$data", fileName)
    }
}
