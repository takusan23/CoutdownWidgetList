package io.github.takusan23.countdownwidgetlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.countdownwidgetlist.BottomFragment.AddEventBottomFragment
import io.github.takusan23.countdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.Room.Init.CountdownDBInit
import io.github.takusan23.countdownwidgetlist.Widget.updateAppWidget
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * RecyclerView関係
     * */
    private val dbItemList = arrayListOf<CountdownDBEntity>()
    private val countdownListAdapter = CountdownAdapter(dbItemList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        GlobalScope.launch {
            loadDB()
        }

        activity_main_fab.setOnClickListener {
            // 予定追加BottomFragment
            val addEventBottomFragment = AddEventBottomFragment()
            addEventBottomFragment.show(supportFragmentManager, "show")
        }

    }

    /**
     * データベースから取り出す。
     * 追加/削除時も呼んでね。（Roomをリアルタイム更新する機能とかあるんか？）
     * */
    suspend fun loadDB() = withContext(Dispatchers.Main) {
        dbItemList.clear()
        withContext(Dispatchers.IO) {
            CountdownDBInit(this@MainActivity).countdownDB.countdownDBDao().getFutureEvent()
        }.sortedBy { countdownDBEntity -> countdownDBEntity.date }.forEach { item ->
            dbItemList.add(item)
        }
        countdownListAdapter.notifyDataSetChanged()
        // Widgetも更新
        updateAppWidget(this@MainActivity)
    }

    private fun initRecyclerView() {
        activity_main_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countdownListAdapter
            // ---ｷﾘﾄﾘｾﾝ---
            val itemDecoration = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
        }
    }
}