package com.karabok.workstep.Fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.karabok.workstep.Utils.FragmentHelper
import com.karabok.workstep.databinding.FragmentFilterBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private var dateAndTime = Calendar.getInstance()

    private var startDate: Long? = null
    private var endDate: Long? = null

    private val MONTH_IN_MILLIS = 2629746000L // приблизительное количество миллисекунд в месяце

    val today = MaterialDatePicker.todayInUtcMilliseconds()
    val sixMonthsForward = today + MONTH_IN_MILLIS * 3 // шесть месяцев вперед от текущей даты

    companion object {
        @JvmStatic
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    // установка обработчика выбора даты
    private var dateSetListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
            setInitialDateTime()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)

        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(today)
            .setEnd(sixMonthsForward)
            .build()

        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Выберите диапазон дат")
            .setSelection(Pair(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
            .setCalendarConstraints(calendarConstraints)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.textInputEditText.isActivated = true
            startDate = selection.first
            endDate = selection.second
            updateDateFieldText(binding.textInputEditText)
            binding.textInputEditText.isActivated = true
        }

        datePicker.addOnCancelListener {
            binding.textInputEditText.isActivated = true
        }

        binding.textInputEditText.setOnClickListener {
            binding.textInputEditText.isActivated = false
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

//        binding.dateButton.setOnClickListener {
//            setDate()
//        }

        binding.backFilter.setOnClickListener {
            FragmentHelper.launchFragment(
                HomeFragment.newInstance(),
                requireActivity().supportFragmentManager.beginTransaction())
        }

    }


    private fun setDate() {
        DatePickerDialog(
            requireContext(), dateSetListener,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        ).show()
    }

    // установка начальных даты и времени
    private fun setInitialDateTime() {
//        binding.dateButton.text = DateUtils.formatDateTime(
//            requireContext(),
//            dateAndTime.timeInMillis,
//            DateUtils.FORMAT_SHOW_DATE
//        )
    }

    private fun updateDateFieldText(dateField: TextInputEditText) {
        // Форматируем даты
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val startDateText = startDate?.let { dateFormat.format(Date(it)) } ?: "Выберите дату"
        val endDateText = endDate?.let { dateFormat.format(Date(it)) } ?: "Выберите дату"

        // Обновляем текст в TextInputEditText
        val text = "$startDateText - $endDateText"
        dateField.setText(text)
    }
}



