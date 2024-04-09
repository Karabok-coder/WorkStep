package com.karabok.workstep.Fragments.CreateOrders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.Fragments.CodeRegFragment
import com.karabok.workstep.R
import com.karabok.workstep.databinding.FragmentCreateOrderList1Binding

class CreateOrderList1 : Fragment() {
    private lateinit var binding: FragmentCreateOrderList1Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateOrderList1Binding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.nextList1.setOnClickListener{
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = CreateOrderList2()

            if (binding.nameCreateOrder.text.toString() == "" ||
                binding.descriptionCreateOrder.text.toString() == "")
            {
                Toast.makeText(activity?.applicationContext, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val bundle: Bundle = Bundle().apply {
                putString(ConstIntent.nameNewOrder, binding.nameCreateOrder.text.toString())
                putString(ConstIntent.descriptionNewOrder, binding.descriptionCreateOrder.text.toString())
                putString(ConstIntent.salaryNewOrder, binding.salaryCreateOrder.text.toString())
            }
            newFragment.arguments = bundle

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateOrderList1()
    }
}