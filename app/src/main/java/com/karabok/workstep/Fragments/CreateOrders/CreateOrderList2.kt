package com.karabok.workstep.Fragments.CreateOrders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.R
import com.karabok.workstep.databinding.FragmentCreateOrderList2Binding

class CreateOrderList2 : Fragment() {
    private lateinit var binding: FragmentCreateOrderList2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateOrderList2Binding.inflate(inflater, container, false)

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.cities))
        binding.autoCompleteCityCreateOrder.setAdapter(adapter)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dataBundle = arguments
        val name = dataBundle?.getString(ConstIntent.nameNewOrder)
        val description = dataBundle?.getString(ConstIntent.descriptionNewOrder)
        val salary = dataBundle?.getString(ConstIntent.salaryNewOrder)

        binding.nextList2.setOnClickListener{
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = CreateOrderList3()

            if (binding.autoCompleteCityCreateOrder.text.toString() == "")
            {
                Toast.makeText(activity?.applicationContext, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val bundle: Bundle = Bundle().apply {
                putString(ConstIntent.nameNewOrder, name)
                putString(ConstIntent.descriptionNewOrder, description)
                putString(ConstIntent.salaryNewOrder, salary)
                putString(ConstIntent.cityNewOrder, binding.autoCompleteCityCreateOrder.text.toString())
            }
            newFragment.arguments = bundle

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }

    companion object {
        fun newInstance() = CreateOrderList2()
    }
}