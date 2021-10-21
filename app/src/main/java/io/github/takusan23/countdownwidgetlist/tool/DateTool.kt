package io.github.takusan23.countdownwidgetlist.tool

import java.text.SimpleDateFormat
import java.util.*

/**
 * UnixTime（ミリ秒）をきれいな時間フォーマットに変換する拡張関数
 * */
fun Long.toTimeFormat(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒")
    return simpleDateFormat.format(this)
}

/**
 * UnixTime（ミリ秒）を日付に直す拡張関数
 * */
fun Long.toDateFormat(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日")
    return simpleDateFormat.format(this)
}

/**
 * UnixTimeを日数に変換する関数
 * */
fun Long.toDay(): Long {
    return this / 1000 / 60 / 60 / 24 // 秒→分→時間→日
}

/**
 * 残り日数を計算する拡張関数。
 * [Int.calcCountdownDay]みたいな感じで使ってね
 * @param isHolidayInclude 休日を含めるか。普通含めないよな。
 * */
fun Long.calcCountdownDay(isHolidayInclude: Boolean = false): Int {
    // 現在の日付
    val currentCalendar = Calendar.getInstance()
    currentCalendar.apply {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    // 引き算して残りのUnixTimeを出す
    val calc = this - currentCalendar.timeInMillis
    // 日数分リピート
    var count = 0
    repeat(calc.milliSecToDay().toInt()) {
        val week = currentCalendar[Calendar.DAY_OF_WEEK]
        if (isHolidayInclude) {
            count++
        } else {
            // 休日を含めない場合は、曜日が日曜か土曜以外のときのみ数える
            if (week != 1 && week != 7) {
                count++
            }
        }
        // 一日足す
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return count
}

/**
 * ミリ秒 -> 日付　変換
 * */
fun Long.milliSecToDay(): Long {
    return this / 1000 / 60 / 60 / 24
}
