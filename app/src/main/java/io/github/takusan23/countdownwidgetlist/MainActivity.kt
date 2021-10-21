package io.github.takusan23.countdownwidgetlist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.countdownwidgetlist.activity.LicenseActivity
import io.github.takusan23.countdownwidgetlist.activity.PreferenceActivity
import io.github.takusan23.countdownwidgetlist.bottomfragment.AddEventBottomFragment
import io.github.takusan23.countdownwidgetlist.room.entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.room.init.CountdownDBInit
import io.github.takusan23.countdownwidgetlist.widget.updateAppWidget
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * RecyclerView関係
     * */
    private val dbItemList = arrayListOf<CountdownDBEntity>()
    private val countdownListAdapter = CountdownAdapter(dbItemList)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        lifecycleScope.launch {
            loadDB()
        }

        activity_main_fab.setOnClickListener {
            // 予定追加BottomFragment
            val addEventBottomFragment = AddEventBottomFragment()
            addEventBottomFragment.show(supportFragmentManager, "show")
        }

    }

    /** メニューを作成する */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    /** メニュー押したとき */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_main_menu_license -> {
                startActivity(Intent(this, LicenseActivity::class.java))
                true
            }
            R.id.activity_main_menu_setting -> {
                startActivity(Intent(this, PreferenceActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     *ウィジェット更新
     * */
    fun loadDB() = updateAppWidget(this@MainActivity)

    private fun initRecyclerView() {
        activity_main_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = countdownListAdapter
            // ---ｷﾘﾄﾘｾﾝ---
            val itemDecoration = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            // データベース監視
            lifecycleScope.launch {
                val flow = CountdownDBInit.getInstance(this@MainActivity).countdownDBDao().flowGetFutureEvent()
                flow.collect { value ->
                    dbItemList.clear()
                    value.forEach {
                        dbItemList.add(it)
                    }
                    withContext(Dispatchers.Main) {
                        countdownListAdapter.notifyDataSetChanged()
                    }
                }

            }
        }
    }
}