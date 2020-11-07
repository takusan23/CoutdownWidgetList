package io.github.takusan23.countdownwidgetlist.BottomFragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.github.takusan23.countdownwidgetlist.MainActivity
import io.github.takusan23.countdownwidgetlist.R
import io.github.takusan23.countdownwidgetlist.Room.Entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.Room.Init.CountdownDBInit
import kotlinx.android.synthetic.main.bottom_fragment_add_event.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 予定追加BottomFragment
 * */
class AddEventBottomFragment : BottomSheetDialogFragment() {

    /** 追加する日時 */
    var addDate = System.currentTimeMillis()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_fragment_add_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_fragment_add_event_add.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                // DBへ入れる
                val isHolidayInclude = bottom_fragment_add_event_holiday.isChecked.toString()
                val description = bottom_fragment_add_event_description.text.toString()
                val entity = CountdownDBEntity(description = description, date = addDate, isHolidayInclude = isHolidayInclude)
                withContext(Dispatchers.IO) {
                    CountdownDBInit.getInstance(requireContext()).countdownDBDao().insert(entity)
                }
                // 追加処理
                Toast.makeText(context, "追加しました", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).loadDB()
                dismiss()
            }
        }

        // 日付指定の日付ピッカー出す
        bottom_fragment_add_event_calendar.setOnClickListener {
            val dialog = MaterialDatePicker.Builder.datePicker().setTitleText("日付指定").build()
            // 決定ボタン
            dialog.addOnPositiveButtonClickListener {
                addDate = it
            }
            dialog.show(childFragmentManager, "date")
        }

    }

}