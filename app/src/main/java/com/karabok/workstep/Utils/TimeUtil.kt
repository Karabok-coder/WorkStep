package com.karabok.workstep.Utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Duration
import java.util.Date
import java.util.Locale

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

        fun yearMonthToDayMonth(inputDateString: String): String {
            // переводит из дд.мм.гггг в гггг.мм.дд

            val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date: Date = inputFormat.parse(inputDateString)

            val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val outputDateString: String = outputFormat.format(date)

            return outputDateString
        }
    }
}