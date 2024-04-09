package com.karabok.workstep.Fragments.CreateOrders

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.R
import com.karabok.workstep.databinding.FragmentCreateOrderList3Binding
import java.util.Calendar

class CreateOrderList3 : Fragment() {
    private lateinit var binding: FragmentCreateOrderList3Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateOrderList3Binding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        fun newInstance() = CreateOrderList3()
    }

    override fun onStart() {
        super.onStart()

        val dataBundle = arguments
        val name = dataBundle?.getString(ConstIntent.nameNewOrder)
        val description = dataBundle?.getString(ConstIntent.descriptionNewOrder)
        val city = dataBundle?.getString(ConstIntent.cityNewOrder)
        val salary = dataBundle?.getString(ConstIntent.salaryNewOrder)

        binding.nextList3.setOnClickListener{
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = CreateOrderList4()

            if (binding.timeEndCreateOrder.text.toString() == "" ||
                binding.timeStartCreateOrder.text.toString() == "" ||
                binding.dateStartCreateOrder.text.toString() == "")
            {
                Toast.makeText(activity?.applicationContext, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val bundle: Bundle = Bundle().apply {
                putString(ConstIntent.nameNewOrder, name)
                putString(ConstIntent.descriptionNewOrder, description)
                putString(ConstIntent.cityNewOrder, city)
                putString(ConstIntent.salaryNewOrder, salary)
                putString(ConstIntent.timeEndNewOrder, binding.timeEndCreateOrder.text.toString())
                putString(ConstIntent.timeStartNewOrder, binding.timeStartCreateOrder.text.toString())
                putString(ConstIntent.dateStartNewOrder, binding.dateStartCreateOrder.text.toString())
            }
            newFragment.arguments = bundle

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }


        binding.timeStartCreateOrder.setOnClickListener {
            showTimePickerDialog(binding.timeStartCreateOrder)
        }

        binding.timeEndCreateOrder.setOnClickListener {
            showTimePickerDialog(binding.timeEndCreateOrder)
        }

        binding.dateStartCreateOrder.setOnClickListener {
            showDatePickerDialog(binding.dateStartCreateOrder)
        }

    }

    private fun showTimePickerDialog(timeEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeEditText.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog(timeEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                timeEditText.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
}