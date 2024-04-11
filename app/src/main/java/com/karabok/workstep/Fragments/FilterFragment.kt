package com.karabok.workstep.Fragments

import android.R
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.Loguru.Luna
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
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)


        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, resources.getStringArray( com.karabok.workstep.R.array.category))
        binding.autoCompleteCategoryFilter.setAdapter(adapter)

        setSubcategoryCreateOrder()

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
            binding.dateFilter.isActivated = true
            startDate = selection.first
            endDate = selection.second
            updateDateFieldText(binding.dateFilter)
            binding.dateFilter.isActivated = true
        }

        datePicker.addOnCancelListener {
            binding.dateFilter.isActivated = true
        }

        binding.dateFilter.setOnClickListener {
            binding.dateFilter.isActivated = false
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.backFilter.setOnClickListener {
            FragmentHelper.launchFragment(
                HomeFragment.newInstance(),
                requireActivity().supportFragmentManager.beginTransaction())
        }

        binding.searchFilterBtn.setOnClickListener {

            Luna.log(binding.dateFilter.text.toString())

            val fragmentManager = activity?.supportFragmentManager
            val newFragment = HomeFragment.newInstance()

            val bundle: Bundle = Bundle().apply {
                putString(ConstIntent.categoryIntent, binding.autoCompleteCategoryFilter.text.toString())
                putString(ConstIntent.subcategoryIntent, binding.autoCompleteSubcategoryFilter.text.toString())
                putString(ConstIntent.cityIntent, binding.autoCompleteCityFilter.text.toString())
                putString(ConstIntent.salaryStartIntent, binding.salaryStart.text.toString())
                putString(ConstIntent.salaryEndIntent, binding.salaryEnd.text.toString())

                if (binding.dateFilter.text.toString() == "" || binding.dateFilter.text.toString() == ""){
                    putString(ConstIntent.dateStartIntent, binding.dateFilter.text.toString())
                    putString(ConstIntent.dateEndIntent, binding.dateFilter.text.toString())
                }
                else{
                    putString(ConstIntent.dateStartIntent, binding.dateFilter.text.toString().split(" - ")[0])
                    putString(ConstIntent.dateEndIntent, binding.dateFilter.text.toString().split(" - ")[1])
                }
            }
            newFragment.arguments = bundle

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(com.karabok.workstep.R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
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

    private fun setSubcategoryCreateOrder() = binding.apply {
        // устанавливает для выпадающих списков настройки
        autoCompleteCategoryFilter.setOnItemClickListener { adapterView, view, i, l ->
            autoCompleteSubcategoryFilter.visibility = View.VISIBLE
            val arrayCategory = resources.getStringArray(com.karabok.workstep.R.array.category)
            when(autoCompleteCategoryFilter.text.toString()){
                arrayCategory[0] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.События_и_развлечения))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[1] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Образование_и_обучение))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[2] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Здоровье_и_красота))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[3] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Ремонт_и_обслуживание))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[4] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Консультирование_и_бизнес))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[5] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Дизайн_и_творчество))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[6] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Веб_разработка))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[7] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Графический_дизайн))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[8] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Письменные_услуги))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[9] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Цифровой_маркетинг))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[10] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.IT_и_программирование))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }

                arrayCategory[11] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(com.karabok.workstep.R.array.Аудио_и_видео_услуги))
                    autoCompleteSubcategoryFilter.setAdapter(adapter)
                }
            }
        }
    }

}



