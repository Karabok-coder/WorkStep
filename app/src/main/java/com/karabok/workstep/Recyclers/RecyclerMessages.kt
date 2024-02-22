package com.karabok.workstep.Recyclers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karabok.workstep.R
import android.view.LayoutInflater
import android.widget.TextView
import com.karabok.workstep.EntityTab.EntityMessage

class RecyclerMessages(private val dataSet: MutableList<EntityMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_MY = 1
    private val TYPE_YOU = 2

    inner class YouMessagesAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text_you)
        val messageDate: TextView = itemView.findViewById(R.id.message_date_you)
    }

    inner class MyMessagesAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.message_text_my)
        val messageDate: TextView = itemView.findViewById(R.id.message_date_my)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MY -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.you_messages_element, parent, false)
                YouMessagesAdapter(view)
            }
            TYPE_YOU -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.my_messages_element, parent, false)
                MyMessagesAdapter(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].sender) {
            1 -> TYPE_MY
            2 -> TYPE_YOU
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_MY -> {
                val viewHolder = holder as YouMessagesAdapter
                viewHolder.messageText.text = dataSet[position].messageText
                viewHolder.messageDate.text = dataSet[position].messageDate
            }
            TYPE_YOU -> {
                val viewHolder = holder as MyMessagesAdapter
                viewHolder.messageText.text = dataSet[position].messageText
                viewHolder.messageDate.text = dataSet[position].messageDate
            }
        }
    }

    override fun getItemCount() = dataSet.size
}