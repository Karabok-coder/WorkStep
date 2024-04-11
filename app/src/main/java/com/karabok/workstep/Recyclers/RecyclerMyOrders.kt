package com.karabok.workstep.Recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karabok.workstep.EntityTab.EntityOrders
import com.karabok.workstep.Interfaces.OnButtonClickListener
import com.karabok.workstep.R
import com.karabok.workstep.Utils.TimeUtil

class RecyclerMyOrders(
    private val order: MutableList<EntityOrders>,
    private var buttonClickListener: OnButtonClickListener
) : RecyclerView.Adapter<RecyclerMyOrders.MyOrderHolder>() {

    class MyOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameWork: TextView = itemView.findViewById(R.id.nameWork)
        val description: TextView = itemView.findViewById(R.id.description)
        val time: TextView = itemView.findViewById(R.id.time)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val fullAddress: TextView = itemView.findViewById(R.id.fullAddress)
        val deleteMyOrder: Button = itemView.findViewById(R.id.deleteMyOrder)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        buttonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_order_item, parent, false)

        return MyOrderHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyOrderHolder, position: Int) {
        val timeStart = TimeUtil.convertSeconds(order[position].timeStart)
        val timeEnd = TimeUtil.convertSeconds(order[position].timeEnd)

        holder.nameWork.text = order[position].nameWork
        holder.description.text = order[position].description
        holder.time.text = "C $timeStart по $timeEnd"
        holder.salary.text = "${order[position].salary} руб"
        holder.fullAddress.text = order[position].city

        holder.deleteMyOrder.setOnClickListener {
            buttonClickListener.onButtonClick(position)
        }
    }

    override fun getItemCount(): Int {
        return order.size
    }
}