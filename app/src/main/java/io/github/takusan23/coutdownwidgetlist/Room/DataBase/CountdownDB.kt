package io.github.takusan23.coutdownwidgetlist.Room.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.takusan23.coutdownwidgetlist.Room.Dao.CountdownDBDao
import io.github.takusan23.coutdownwidgetlist.Room.Entity.CountdownDBEntity

/**
 * データベース
 * */
@Database(entities = [CountdownDBEntity::class], version = 1)
abstract class CountdownDB : RoomDatabase() {
    abstract fun countdownDBDao(): CountdownDBDao
}
