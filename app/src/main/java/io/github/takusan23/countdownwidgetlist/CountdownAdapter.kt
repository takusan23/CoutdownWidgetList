package io.github.takusan23.countdownwidgetlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.takusan23.countdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.Room.Init.CountdownDBInit
import io.github.takusan23.countdownwidgetlist.Tool.calcCountdownDay
import io.github.takusan23.countdownwidgetlist.Tool.toTimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 一覧表示で使う
 * */
class CountdownAdapter(val dbItemList: ArrayList<CountdownDBEntity>) : RecyclerView.Adapter<CountdownAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView = itemView.findViewById<TextView>(R.id.adapter_description)
        val dateTextView = itemView.findViewById<TextView>(R.id.adapter_date)
        val countdownTextView = itemView.findViewById<TextView>(R.id.adapter_countdown)
        val deleteButton = itemView.findViewById<ImageView>(R.id.adapter_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dbItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val context = holder.dateTextView.context
            val item = dbItemList[position]
            // テキスト入れる
            descriptionTextView.text = item.description
            dateTextView.text = item.date.toTimeFormat()
            countdownTextView.text = "残り\n${item.date.calcCountdownDay(item.isHolidayInclude.toBoolean())}日"
            // 削除ボタン
            deleteButton.setOnClickListener {
                Snackbar.make(it, "削除していいですか？", Snackbar.LENGTH_SHORT).setAction("削除") {
                    // 削除する
                    GlobalScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            CountdownDBInit.getInstance(context).countdownDBDao().delete(item)
                        }
                        Toast.makeText(context, "削除しました", Toast.LENGTH_SHORT).show()
                        (context as MainActivity).loadDB()
                    }
                }.show()
            }
        }
    }

}
