package com.karabok.workstep.Recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karabok.workstep.EntityTab.EntityOrders
import com.karabok.workstep.R
import com.karabok.workstep.Utils.TimeUtil

class RecyclerOrder(
    private val order: MutableList<EntityOrders>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerOrder.OrderHolder>() {

    class OrderHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val nameWork: TextView = itemView.findViewById(R.id.nameWork)
        val description: TextView = itemView.findViewById(R.id.description)
        val time: TextView = itemView.findViewById(R.id.time)
        val salary: TextView = itemView.findViewById(R.id.salary)
        val fullAddress: TextView = itemView.findViewById(R.id.fullAddress)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_item, parent, false)

        return OrderHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val timeStart = TimeUtil.convertSeconds(order[position].timeStart)
        val timeEnd = TimeUtil.convertSeconds(order[position].timeEnd)

        holder.nameWork.text = order[position].nameWork
        holder.description.text = order[position].description
        holder.time.text = "C $timeStart по $timeEnd"
        holder.salary.text = "${order[position].salary} руб"
        holder.fullAddress.text = order[position].cityName
    }



    override fun getItemCount(): Int {
        return order.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}