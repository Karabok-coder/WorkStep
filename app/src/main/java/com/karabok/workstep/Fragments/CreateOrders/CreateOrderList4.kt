package com.karabok.workstep.Fragments.CreateOrders

import android.content.Context
import android.os.Bundle
import android.view.FrameMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.Fragments.HomeFragment
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.R
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.Utils.Requests
import com.karabok.workstep.Utils.TimeUtil
import com.karabok.workstep.databinding.FragmentCreateOrderList4Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class CreateOrderList4 : Fragment() {
    private lateinit var binding: FragmentCreateOrderList4Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateOrderList4Binding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        fun newInstance() = CreateOrderList4()
    }

    override fun onStart() {
        super.onStart()

        setSubcategoryCreateOrder()

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.category))
        binding.autoCompleteCategoryCreateOrder.setAdapter(adapter)

        binding.finishList4.setOnClickListener{
            val dataBundle = arguments
            val nameWork = dataBundle?.getString(ConstIntent.nameNewOrder)
            val description = dataBundle?.getString(ConstIntent.descriptionNewOrder)
            val timeEnd = dataBundle?.getString(ConstIntent.timeEndNewOrder)
            val timeStart = dataBundle?.getString(ConstIntent.timeStartNewOrder)
            val dateStart = dataBundle?.getString(ConstIntent.dateStartNewOrder)
            val city = dataBundle?.getString(ConstIntent.cityNewOrder)
            val salary = dataBundle?.getString(ConstIntent.salaryNewOrder)

            if (binding.autoCompleteCategoryCreateOrder.text.toString() == "")
            {
                Toast.makeText(activity?.applicationContext, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val category = binding.autoCompleteCategoryCreateOrder.text.toString()
            val subcategory = binding.autoCompleteSubcategoryCreateOrder.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    val sharedPreferences = activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)
                    val token = LoginToken.getToken(sharedPreferences)

                    Requests.post(
                        ConstAPI.domainLink + ConstAPI.insertOrder,
                        JSONObject()
                            .put("nameWork", nameWork)
                            .put("timeStart", timeStart)
                            .put("timeEnd", timeEnd)
                            .put("description", description)
                            .put("salary", salary)
                            .put("city", city)
                            .put("userAuthor", token?.split("__")?.get(0)?.toInt())
                            .put("timePublish", TimeUtil.getMoscowTime())
                            .put("category", category)
                            .put("subcategory", subcategory)
                            .put("dateStart", dateStart?.let { it1 -> TimeUtil.yearMonthToDayMonth(it1) })
                            .toString()
                    )
                }.join()
            }
        }
    }

    private fun setSubcategoryCreateOrder() = binding.apply {
        // устанавливает для выпадающих списков настройки
        autoCompleteCategoryCreateOrder.setOnItemClickListener { adapterView, view, i, l ->
            autoCompleteSubcategoryCreateOrder.visibility = VISIBLE
            val arrayCategory = resources.getStringArray(R.array.category)
            when(autoCompleteCategoryCreateOrder.text.toString()){
                arrayCategory[0] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.События_и_развлечения))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[1] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Образование_и_обучение))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[2] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Здоровье_и_красота))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[3] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Ремонт_и_обслуживание))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[4] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Консультирование_и_бизнес))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[5] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Дизайн_и_творчество))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[6] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Веб_разработка))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[7] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Графический_дизайн))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[8] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Письменные_услуги))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[9] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Цифровой_маркетинг))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[10] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.IT_и_программирование))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }

                arrayCategory[11] -> {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        resources.getStringArray(R.array.Аудио_и_видео_услуги))
                    autoCompleteSubcategoryCreateOrder.setAdapter(adapter)
                }
            }
        }
    }

}