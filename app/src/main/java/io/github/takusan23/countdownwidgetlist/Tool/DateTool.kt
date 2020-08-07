package io.github.takusan23.countdownwidgetlist.Tool

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
 * 残り日数（といいながら秒）を計算する拡張関数。
 * [予定日時.calcCountdownSec]みたいな感じで使ってね
 * */
fun Long.calcCountdownSec(): Long {
    val localDate = Calendar.getInstance()
    localDate.apply {
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    // 引き算して残りのUnixTimeを出す
    val calc = this - localDate.timeInMillis
    return calc / 1000
}

/**
 * 残り日数を計算する
 * [予定日時.calcCountdownDay]みたいな感じで使ってね
 * */
fun Long.calcCountdownDay(): Long {
    val sec = this.calcCountdownSec()
    return sec / 60 / 60 / 24 // 分→時間→日
}