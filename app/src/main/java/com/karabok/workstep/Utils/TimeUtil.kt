package com.karabok.workstep.Utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Duration

class TimeUtil {
    companion object {
        // Функция для получения локального времени с учетом временной зоны установленной на телефоне
        fun getLocalTime(): LocalDateTime {
            return LocalDateTime.now()
        }

        // Функция для получения московского времени
        fun getMoscowTime(): LocalDateTime {
            val moscowTimeZone = ZoneId.of("Europe/Moscow")
            return LocalDateTime.now(moscowTimeZone)
        }

        // Метод для перевода секунд в часы и минуты
        fun convertSeconds(seconds: Int): String {
            val duration = Duration.ofSeconds(seconds.toLong())
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            var textTime: String = ""

            textTime += if (hours < 10L) {
                "0${hours}:"
            } else {
                "${hours}:"
            }

            textTime += if (minutes == 0L) {
                "${minutes}0"
            } else {
                "${minutes}"
            }

            return textTime
        }
    }
}