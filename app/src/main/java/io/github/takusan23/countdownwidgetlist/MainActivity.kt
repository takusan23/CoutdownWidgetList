package io.github.takusan23.countdownwidgetlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.takusan23.countdownwidgetlist.activity.LicenseActivity
import io.github.takusan23.countdownwidgetlist.activity.PreferenceActivity
import io.github.takusan23.countdownwidgetlist.bottomfragment.AddEventBottomFragment
import io.github.takusan23.countdownwidgetlist.databinding.ActivityMainBinding
import io.github.takusan23.countdownwidgetlist.room.entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.room.init.CountdownDBInit
import io.github.takusan23.countdownwidgetlist.widget.updateAppWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    /**
     * RecyclerView関係
     * */
    private val dbItemList = arrayListOf<CountdownDBEntity>()
    private val countdownListAdapter = CountdownAdapter(dbItemList)

    /** kotlin-android-extensions から ViewBinding に */
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        initRecyclerView()

        lifecycleScope.launch {
            loadDB()
        }

        viewBinding.activityMainFab.setOnClickListener {
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
        viewBinding.activityMainRecyclerview.apply {
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