package io.github.takusan23.coutdownwidgetlist.Tool

import java.text.SimpleDateFormat

/**
 * UnixTimeをきれいな時間フォーマットに変換する拡張関数
 * */
fun Long.toTimeFormat(): String? {
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒")
    return simpleDateFormat.format(this)
}