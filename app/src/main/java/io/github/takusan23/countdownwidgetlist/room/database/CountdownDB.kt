package io.github.takusan23.countdownwidgetlist.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.takusan23.countdownwidgetlist.room.dao.CountdownDBDao
import io.github.takusan23.countdownwidgetlist.room.entity.CountdownDBEntity

/**
 * データベース
 * */
@Database(entities = [CountdownDBEntity::class], version = 2, exportSchema = false)
abstract class CountdownDB : RoomDatabase() {
    abstract fun countdownDBDao(): CountdownDBDao
}
