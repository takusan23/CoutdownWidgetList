package io.github.takusan23.countdownwidgetlist.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import androidx.core.graphics.ColorUtils
import androidx.preference.PreferenceManager
import io.github.takusan23.countdownwidgetlist.R

/**
 * Implementation of App Widget functionality.
 */
class CountdownListWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

/**
 * Widget更新関数
 * @param context こんてきすと。ActivityやServiceからでも更新可能
 * */
internal fun updateAppWidget(context: Context) {

    // WidgetのView
    val views = RemoteViews(context.packageName, R.layout.countdown_list_widget)
    // ListView
    val remoteViewsFactoryIntent = Intent(context, ListViewWidgetService::class.java)
    views.setRemoteAdapter(R.id.widget_listview, remoteViewsFactoryIntent)

    val prefSetting = PreferenceManager.getDefaultSharedPreferences(context)
    if (prefSetting.getBoolean("is_custom_widget", false)) {
        // 設定値読み出し
        val colorCode = prefSetting.getString("widget_background_color", "#ffffff")
        val alpha = (prefSetting.getString("widget_alpha", "20")?.toInt() ?: 20) / 100f
        val parseColor = Color.parseColor(colorCode)
        val color = ColorUtils.setAlphaComponent(parseColor, (255 * alpha).toInt())
        views.setInt(R.id.widget_root, "setBackgroundColor", color)
    } else {
        views.setInt(R.id.widget_root, "setBackgroundResource", R.drawable.widget_background)
    }

    // Contextあれば更新できる！
    val componentName = ComponentName(context, CountdownListWidget::class.java)
    val manager = AppWidgetManager.getInstance(context)
    val ids = manager.getAppWidgetIds(componentName)
    ids.forEach { id ->
        // 更新
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(id, R.id.widget_listview) // ListView更新
        manager.updateAppWidget(id, views)
    }

}