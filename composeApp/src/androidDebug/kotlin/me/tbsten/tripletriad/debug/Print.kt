package me.tbsten.tripletriad.debug

import android.util.Log

private val DebugPrintTag = "tripletriad-debug-print"

fun debugPrint(message: () -> Any?) {
    Log.d(DebugPrintTag, "${message()}")
}
