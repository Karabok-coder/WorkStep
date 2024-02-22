package com.karabok.workstep.Loguru

import android.util.Log
import com.karabok.workstep.Const.ConstApp

object Luna {
    fun log(logData: String){
        val stackTrace: Array<StackTraceElement> = Thread.currentThread().stackTrace
        val calling: StackTraceElement = stackTrace[3]
//        val currentTime: LocalDateTime = LocalDateTime.now()

        val textLog: String =
            "${calling.fileName} " +
            "${calling.methodName} " +
            "${calling.lineNumber} - " +
            logData

        Log.d(ConstApp.tagLog, textLog)
    }

    fun error(logData: String){
        val stackTrace: Array<StackTraceElement> = Thread.currentThread().stackTrace
        val calling: StackTraceElement = stackTrace[3]
//        val currentTime: LocalDateTime = LocalDateTime.now()

        val textLog: String =
            "${calling.fileName} " +
                    "${calling.methodName} " +
                    "${calling.lineNumber} - " +
                    logData

        Log.e(ConstApp.tagLog, textLog)
    }
}