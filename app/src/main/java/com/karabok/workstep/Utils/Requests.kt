package com.karabok.workstep.Utils

import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Loguru.Luna
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object Requests {
    fun get(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var inputLine: String?
                val response = StringBuilder()

                while (reader.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                reader.close()
                return response.toString()
            }
            else {
                Luna.error("GET request failed")
            }
        }
        catch (e: MalformedURLException) {
            Luna.error("Malformed URL: ${e.message}")
        }
        catch (e: IOException) {
            Luna.error("IO Exception: ${e.message}")
        }
        finally {
            connection.disconnect()
        }
        return ""
    }

    fun get(urlString: String, data: String) : String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.write(data.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var inputLine: String?
                val response = StringBuilder()

                while (reader.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                reader.close()

                return response.toString()
            }
            else {
                Luna.error("GET request failed")
            }
        }
        catch (e: MalformedURLException) {
            Luna.error("Malformed URL: ${e.message}")
        }
        catch (e: IOException) {
            Luna.error("IO Exception: ${e.message}")
        }
        finally {
            connection.disconnect()
        }

        return ""
    }

    fun post(urlString: String, data: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.write(data.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuffer()
                var inputLine: String?
                while (inputStream.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                inputStream.close()
                return response.toString()
            } else {
                Luna.error("Exception: $responseCode")
            }
        } catch (e: IOException) {
            Luna.error("Exception: ${e.message}")
        } finally {
            connection.disconnect()
        }
        return ""
    }

    fun patch(urlString: String, data: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PATCH"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.write(data.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuffer()
                var inputLine: String?
                while (inputStream.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                inputStream.close()
                return response.toString()
            } else {
                Luna.error("Exception: $responseCode")
            }
        } catch (e: IOException) {
            Luna.error("Exception: ${e.message}")
        } finally {
            connection.disconnect()
        }
        return ""
    }

    fun delete(urlString: String, data: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"
        connection.setRequestProperty("Content-Type", "application/json")

        try {
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.write(data.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuffer()
                var inputLine: String?
                while (inputStream.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                inputStream.close()
                return response.toString()
            } else {
                Luna.error("Exception: $responseCode")
            }
        } catch (e: IOException) {
            Luna.error("Exception: ${e.message}")
        } finally {
            connection.disconnect()
        }
        return ""
    }
}