package io.github.takusan23.coutdownwidgetlist.Widget

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.github.takusan23.coutdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.coutdownwidgetlist.R
import io.github.takusan23.coutdownwidgetlist.Room.Init.CountdownDBInit
import io.github.takusan23.coutdownwidgetlist.Tool.toTimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * WidgetのListViewに更新する関数
 * */
class ListViewWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return ListViewWidgetFactory()
    }

    private inner class ListViewWidgetFactory : RemoteViewsFactory {

        private val countDownList = arrayListOf<CountdownDBEntity>()

        override fun onCreate() {

        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        // データ取得
        override fun onDataSetChanged() {
            runBlocking {
                countDownList.clear()
                withContext(Dispatchers.IO) {
                    CountdownDBInit(applicationContext).countdownDB.countdownDBDao().getAll()
                }.forEach { item ->
                    countDownList.add(item)
                }
            }
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        // ListViewの各View
        override fun getViewAt(p0: Int): RemoteViews {
            val dbItem = countDownList[p0]
            val views = RemoteViews(applicationContext.packageName, R.layout.countdown_list_widget)
            views.apply {
                setTextViewText(R.id.widget_item_description, dbItem.description)
                setTextViewText(R.id.widget_item_date, dbItem.date.toTimeFormat()) // toTimeFormatは拡張関数
                setProgressBar(R.id.widget_item_progress, dbItem.date.toInt(), System.currentTimeMillis().toInt(), false) // 進捗
            }
            return views
        }

        override fun getCount(): Int {
            return countDownList.size
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun onDestroy() {

        }

    }

}
