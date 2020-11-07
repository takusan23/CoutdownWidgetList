package io.github.takusan23.countdownwidgetlist.Widget

import android.content.Intent
import android.graphics.Paint
import android.service.autofill.CharSequenceTransformation
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.text.HtmlCompat
import io.github.takusan23.countdownwidgetlist.R
import io.github.takusan23.countdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.Room.Init.CountdownDBInit
import io.github.takusan23.countdownwidgetlist.Tool.calcCountdownDay
import io.github.takusan23.countdownwidgetlist.Tool.toDateFormat
import kotlinx.android.synthetic.main.widget_list_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * WidgetのListViewで使うAdapter
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
                    CountdownDBInit.getInstance(applicationContext).countdownDBDao().getFutureEvent()
                }.sortedBy { countdownDBEntity -> countdownDBEntity.date }.forEach { item ->
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
            val views = RemoteViews(applicationContext.packageName, R.layout.widget_list_layout)
            val day = dbItem.date.calcCountdownDay(dbItem.isHolidayInclude.toBoolean())
            views.apply {
                setTextViewText(R.id.widget_list_description, dbItem.description)
                setTextViewText(R.id.widget_list_date, dbItem.date.toDateFormat()) // toTimeFormatは拡張関数
                setTextViewText(R.id.widget_list_countdown, "残り ${day}日")
                setTextViewText(R.id.widget_list_holiday, HtmlCompat.fromHtml("<font color=blue>土</font><font color=red>日</font>", HtmlCompat.FROM_HTML_MODE_COMPACT))
                // 土日を含めない場合は打ち消す
                if (!dbItem.isHolidayInclude.toBoolean()) {
                    // なんかリフレクションみたいなことやってる
                    setInt(R.id.widget_list_holiday, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                } else {
                    // ListView系はif書いたらelseもちゃんと書かないといけない
                    setInt(R.id.widget_list_holiday, "setPaintFlags", 0)
                }
                // 365超えなければプログレスバー表示など
                if (365 >= day) {
                    setProgressBar(R.id.widget_list_progress, 365, 365 - day, false)
                    setTextViewText(R.id.widget_list_percent, "現在 ${ 100 - ((day / 365f) * 100).toInt()} %")
                    setViewVisibility(R.id.widget_list_progress, View.VISIBLE)
                } else {
                    setViewVisibility(R.id.widget_list_progress, View.GONE)
                }
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
