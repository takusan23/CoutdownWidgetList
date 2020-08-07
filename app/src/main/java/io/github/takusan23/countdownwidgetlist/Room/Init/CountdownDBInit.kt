package io.github.takusan23.countdownwidgetlist.Room.Init

import android.content.Context
import androidx.room.Room
import io.github.takusan23.countdownwidgetlist.Room.DataBase.CountdownDB

/**
 * データベースを準備する関数
 * @param context こんてきすと
 * */
class CountdownDBInit(context: Context) {
    val countdownDB = Room.databaseBuilder(context, CountdownDB::class.java, "CountdownDB.db").build()
}