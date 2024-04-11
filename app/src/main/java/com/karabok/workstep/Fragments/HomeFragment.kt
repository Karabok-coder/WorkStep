package com.karabok.workstep.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityOrders
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.Recyclers.RecyclerOrder
import com.karabok.workstep.Utils.FragmentHelper
import com.karabok.workstep.Utils.Requests
import com.karabok.workstep.Utils.TimeUtil
import com.karabok.workstep.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HomeFragment : Fragment(), RecyclerOrder.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding

    private var adapter: RecyclerOrder? = null
    private var orders: MutableList<EntityOrders> = mutableListOf()

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        loadListOrders()

        binding.filterButton.setOnClickListener {
            FragmentHelper.launchFragment(
                FilterFragment.newInstance(),
                requireActivity().supportFragmentManager.beginTransaction()
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadListOrders()
        }
    }

    private fun loadListOrders(){
        orders = mutableListOf()
        binding.swipeRefresh.isRefreshing = true

        val dataBundle = arguments

        Luna.log(dataBundle.toString())
        if (dataBundle != null) {
            bundleFillList(dataBundle)
        }
        else{
            fillList()
        }

        binding.swipeRefresh.isRefreshing = false
    }

    private fun bundleFillList(dataBundle: Bundle){
        val category = dataBundle.getString(ConstIntent.categoryIntent)
        val subcategory = dataBundle.getString(ConstIntent.subcategoryIntent)
        val city = dataBundle.getString(ConstIntent.cityIntent)

        var salaryStart: Int? = 0
        var salaryEnd: Int? = 0
        var dateStart: String? = ""
        var dateEnd: String? = ""

        if (dataBundle.getString(ConstIntent.salaryStartIntent) != ""){
            salaryStart = dataBundle.getString(ConstIntent.salaryStartIntent)?.toInt()
        }

        if (dataBundle.getString(ConstIntent.salaryEndIntent) != ""){
            salaryEnd = dataBundle.getString(ConstIntent.salaryEndIntent)?.toInt()
        }

        if (dataBundle.getString(ConstIntent.dateStartIntent) != "" || dataBundle.getString(ConstIntent.dateEndIntent) != ""){
            dateStart = TimeUtil.yearMonthToDayMonth(dataBundle.getString(ConstIntent.dateStartIntent))
            dateEnd = TimeUtil.yearMonthToDayMonth(dataBundle.getString(ConstIntent.dateEndIntent))
        }

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                val ordersReq = Requests.post(
                    ConstAPI.domainLink + ConstAPI.selectOrdersFilter,
                    JSONObject()
                        .put("category", category)
                        .put("subcategory", subcategory)
                        .put("city", city)
                        .put("salaryStart", salaryStart)
                        .put("salaryEnd", salaryEnd)
                        .put("dateStart", dateStart)
                        .put("dateEnd", dateEnd)
                        .toString()
                )

                val jsonObject =
                    JsonParser.parseString(ordersReq).asJsonObject.get("content").asJsonObject

                for ((key, value) in jsonObject.entrySet()) {
                    val orderJson = jsonObject.get(key).asJsonObject
                    orders.add(
                        EntityOrders(
                            orderJson["id"].toString().toInt(),
                            orderJson["nameWork"].toString().trim('"'),
                            orderJson["timeStart"].toString().toInt(),
                            orderJson["timeEnd"].toString().toInt(),
                            orderJson["description"].toString().trim('"'),
                            orderJson["salary"].toString().toInt(),
                            orderJson["city"].toString().trim('"'),
                            orderJson["timePublish"].toString().trim('"'),
                            orderJson["userAuthor"].toString().toInt(),
                            orderJson["category"].toString().trim('"'),
                            orderJson["subcategory"].toString().trim('"'),
                            orderJson["dateStart"].toString().trim('"')
                        )
                    )
                }
                withContext(Dispatchers.Main){
                    setRecycler()
                }
            }.join()
        }

    }

    private fun fillList() {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                val ordersReq = RequestDbApi.select(ConstAPI.allOrders)
                val jsonObject =
                    JsonParser.parseString(ordersReq).asJsonObject.get("content").asJsonObject

                for ((key, value) in jsonObject.entrySet()) {
                    val orderJson = jsonObject.get(key).asJsonObject
                    orders.add(
                        EntityOrders(
                            orderJson["id"].toString().toInt(),
                            orderJson["nameWork"].toString().trim('"'),
                            orderJson["timeStart"].toString().toInt(),
                            orderJson["timeEnd"].toString().toInt(),
                            orderJson["description"].toString().trim('"'),
                            orderJson["salary"].toString().toInt(),
                            orderJson["city"].toString().trim('"'),
                            orderJson["timePublish"].toString().trim('"'),
                            orderJson["userAuthor"].toString().toInt(),
                            orderJson["category"].toString().trim('"'),
                            orderJson["subcategory"].toString().trim('"'),
                            orderJson["dateStart"].toString().trim('"')
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    setRecycler()
                }
            }.join()
        }
    }

    private fun setRecycler(){
        if (orders.size > 0) {
            binding.apply {
                ordersRecycler.visibility = View.VISIBLE
                binding.textEmptyList.visibility = View.GONE
                ordersRecycler.layoutManager = LinearLayoutManager(activity)
                adapter = RecyclerOrder(orders, this@HomeFragment)
                ordersRecycler.adapter = adapter
                swipeRefresh.isRefreshing = false
                ordersRecycler.scrollToPosition(0)
            }
        } else {
            binding.apply {
                ordersRecycler.visibility = View.GONE
                binding.textEmptyList.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onItemClick(position: Int) {
        val orderFragment = OrderFragment.newInstance()

        val bundle: Bundle = Bundle().apply {
            putParcelable(ConstIntent.entityBundle, orders[position])
        }
        orderFragment.arguments = bundle

        FragmentHelper.launchFragment(
            orderFragment,
            requireActivity().supportFragmentManager.beginTransaction()
        )
    }

}