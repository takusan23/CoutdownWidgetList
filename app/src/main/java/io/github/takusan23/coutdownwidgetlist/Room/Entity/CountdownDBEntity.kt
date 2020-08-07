package io.github.takusan23.coutdownwidgetlist.Room.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * カウントダウンデータベースに入れる内容
 * @param day 日付。多分UnixTimeのミリ秒なのでUnixTimeに変換するなら1000で割る
 * @param description メモ的な何か
 * @param id 主キー
 * */
@Entity(tableName = "countdown_list")
data class CountdownDBEntity(
    @ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: Long
)