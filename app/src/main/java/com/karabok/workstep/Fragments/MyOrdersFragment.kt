package com.karabok.workstep.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityOrders
import com.karabok.workstep.Interfaces.OnButtonClickListener
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.Recyclers.RecyclerMyOrders
import com.karabok.workstep.Recyclers.RecyclerOrder
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.FragmentMyOrdersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding

    private var adapter: RecyclerMyOrders? = null
    private var orders: MutableList<EntityOrders> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyOrdersFragment()
    }

    override fun onStart() {
        super.onStart()
        loadMyOrders()
    }

    private fun loadMyOrders() {
        binding.progressMyOrders.visibility = VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                val sharedPreferences =
                    activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

                val token = LoginToken.getToken(sharedPreferences)
                val userId = token?.split("__")?.get(0)?.toInt()

                orders = mutableListOf()

                val orderReq = RequestDbApi.select(
                    ConstAPI.selectOrderUserId,
                    "userId=$userId"
                )

                val jsonObject =
                    JsonParser.parseString(orderReq).asJsonObject.get("content").asJsonObject
                for ((key, value) in jsonObject.entrySet()) {
                    val userJson = jsonObject.get(key).asJsonObject
                    orders.add(EntityOrders(
                        userJson["id"].toString().toInt(),
                        userJson["nameWork"].toString().trim('"'),
                        userJson["timeStart"].toString().toInt(),
                        userJson["timeEnd"].toString().toInt(),
                        userJson["description"].toString().trim('"'),
                        userJson["salary"].toString().toInt(),
                        userJson["city"].toString().trim('"'),
                        userJson["timePublish"].toString().trim('"'),
                        userJson["userAuthor"].toString().toInt(),
                        userJson["category"].toString().trim('"'),
                        userJson["subcategory"].toString().trim('"'),
                        userJson["dateStart"].toString().trim('"')
                    ))
                }

                requireActivity().runOnUiThread {
                    if(orders.size > 0){
                        binding.apply {
                            myOrdersRecycler.visibility = View.VISIBLE
                            binding.textEmptyList.visibility = View.GONE
                            myOrdersRecycler.layoutManager = LinearLayoutManager(activity)
                            adapter = RecyclerMyOrders(orders, object : OnButtonClickListener {
                                override fun onButtonClick(position: Int) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        launch {
                                            val order = orders[position]
                                            RequestDbApi.select(
                                                ConstAPI.deleteOrderId,
                                                "orderId=${order.id}"
                                            )

                                        }.join()
                                    }
                                    Toast.makeText(activity?.applicationContext, "Успешно удалено", Toast.LENGTH_SHORT).show();
                                    loadMyOrders()
                                }
                            })
                            myOrdersRecycler.adapter = adapter
                            myOrdersRecycler.scrollToPosition(0)
                        }
                    }
                    else {
                        binding.apply {
                            myOrdersRecycler.visibility = View.GONE
                            binding.textEmptyList.visibility = View.VISIBLE
                        }
                    }

                    binding.progressMyOrders.visibility = INVISIBLE
                }
            }.join()
        }


    }


}