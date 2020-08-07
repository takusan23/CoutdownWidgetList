package io.github.takusan23.coutdownwidgetlist.Room.Dao

import androidx.room.*
import io.github.takusan23.coutdownwidgetlist.Room.Entity.CountdownDBEntity

@Dao
interface CountdownDBDao {
    /** 全データ取得 */
    @Query("SELECT * FROM list")
    fun getAll(): List<CountdownDBEntity>

    /** データ更新 */
    @Update
    fun update(countdownDBEntity: CountdownDBEntity)

    /** データ追加 */
    @Insert
    fun insert(countdownDBEntity: CountdownDBEntity)

    /** データ削除 */
    @Delete
    fun delete(countdownDBEntity: CountdownDBEntity)

    /** IDを使って削除する */
    @Query("DELETE FROM countdown_list WHERE _id = :id")
    fun deleteById(id: Int)

    /** IDを使ってデータベースから取得する */
    @Query("SELECT * FROM countdown_list WHERE _id = :id")
    fun findById(id: Int): CountdownDBEntity?
}