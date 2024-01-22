package io.github.takusan23.countdownwidgetlist.bottomfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.github.takusan23.countdownwidgetlist.MainActivity
import io.github.takusan23.countdownwidgetlist.databinding.BottomFragmentAddEventBinding
import io.github.takusan23.countdownwidgetlist.room.entity.CountdownDBEntity
import io.github.takusan23.countdownwidgetlist.room.init.CountdownDBInit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 予定追加BottomFragment
 * */
class AddEventBottomFragment : BottomSheetDialogFragment() {

    /** 追加する日時 */
    var addDate = System.currentTimeMillis()

    /** kotlin-android-extensions から ViewBinding に */
    private var _binding: BottomFragmentAddEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomFragmentAddEventBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomFragmentAddEventAdd.setOnClickListener {
            lifecycleScope.launch {
                // DBへ入れる
                val isHolidayInclude = binding.bottomFragmentAddEventHoliday.isChecked.toString()
                val description = binding.bottomFragmentAddEventDescription.text.toString()
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
        binding.bottomFragmentAddEventCalendar.setOnClickListener {
            val dialog = MaterialDatePicker.Builder.datePicker().setTitleText("日付指定").build()
            // 決定ボタン
            dialog.addOnPositiveButtonClickListener {
                addDate = it
            }
            dialog.show(childFragmentManager, "date")
        }

    }

}