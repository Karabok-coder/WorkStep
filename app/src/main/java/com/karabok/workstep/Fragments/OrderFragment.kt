package com.karabok.workstep.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityOrders
import com.karabok.workstep.EntityTab.EntityProfile
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.R
import com.karabok.workstep.Utils.FragmentHelper
import com.karabok.workstep.Utils.TimeUtil
import com.karabok.workstep.databinding.FragmentOrderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private var order: EntityOrders? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            order = bundle.getParcelable(ConstIntent.entityBundle)
            settingOrder(order!!)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.backOrder.setOnClickListener {
            FragmentHelper.launchFragment(
                HomeFragment.newInstance(),
                requireActivity().supportFragmentManager.beginTransaction())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()
    }

    private fun settingOrder(item: EntityOrders) = binding.apply {
        val timeStart = TimeUtil.convertSeconds(item.timeStart)
        val timeEnd = TimeUtil.convertSeconds(item.timeEnd)

        nameWork.text = item.nameWork
        idWork.text = "Номер объявления ${item.id.toString()}"
        timeWork.text = "C $timeStart по $timeEnd"
        description.text = item.description
        salary.text = "${item.salary} руб"
        fullAddress.text = item.cityName
    }
}