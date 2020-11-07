package io.github.takusan23.countdownwidgetlist.Room.Init

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.takusan23.countdownwidgetlist.Room.DataBase.CountdownDB

/**
 * データベースを準備する関数。シングルトン（アプリ内でインスタンスを使い回す？）じゃないとFlowの通知が来ない
 * @param context こんてきすと
 * */

private lateinit var countdownDB: CountdownDB

class CountdownDBInit {

    companion object {
        fun getInstance(context: Context): CountdownDB {
            if (!::countdownDB.isInitialized) {
                // 初期化してない
                countdownDB = Room.databaseBuilder(context, CountdownDB::class.java, "CountdownDB.db")
                    .addMigrations(object : Migration(1, 2) {
                        // 土日をカウントするかどうかのカラムを追加するためマイグレーション書く
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("""
                                ALTER TABLE countdown_list ADD COLUMN holiday_include TEXT
                            """.trimIndent())
                        }
                    })
                    .build()
            }
            return countdownDB
        }
    }
}