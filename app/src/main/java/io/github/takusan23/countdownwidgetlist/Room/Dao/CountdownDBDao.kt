package io.github.takusan23.countdownwidgetlist.Room.Dao

import androidx.room.*
import io.github.takusan23.countdownwidgetlist.Room.Entity.CountdownDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownDBDao {
    /** 全データ取得 */
    @Query("SELECT * FROM countdown_list")
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

    /** 未来の値のみを取り出す */
    @Query("SELECT * FROM countdown_list WHERE date > :time")
    fun getFutureEvent(time: Long = System.currentTimeMillis()): List<CountdownDBEntity>

    @Query("SELECT * FROM countdown_list")
    fun flowGetFutureEvent(): Flow<List<CountdownDBEntity>>

}