package io.github.takusan23.coutdownwidgetlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.takusan23.coutdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.coutdownwidgetlist.Tool.toTimeFormat
import java.util.ArrayList

class CountdownAdapter(val dbItemList: ArrayList<CountdownDBEntity>) : RecyclerView.Adapter<CountdownAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView= itemView.findViewById<TextView>(R.id.adapter_description)
        val dateTextView=itemView.findViewById<TextView>(R.id.adapter_date)
        val progress = itemView.findViewById<ProgressBar>(R.id.adapter_progress)
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

            descriptionTextView.text = item.description
            dateTextView.text = item.date.toTimeFormat()
            progress.max = item.date.toInt()
            progress.progress = System.currentTimeMillis().toInt()

        }
    }

}
