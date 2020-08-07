package io.github.takusan23.coutdownwidgetlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.coutdownwidgetlist.Room.DataBase.CountdownDB
import io.github.takusan23.coutdownwidgetlist.Room.Entity.CountdownDBEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    val dbItemList = arrayListOf<CountdownDBEntity>()
    val countdownListAdapter= CountdownAdapter(dbItemList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        // 追加
        GlobalScope.launch {

            val calendar = Calendar.getInstance()
            calendar.set(2021,4,6)

            val item = CountdownDBEntity(description = "卒業",date = calendar.time.time)
        }

    }

    private fun initRecyclerView() {
        activity_main_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager  = LinearLayoutManager(this@MainActivity)
            adapter = countdownListAdapter
        }
    }
}