package com.karabok.workstep.Recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karabok.workstep.EntityTab.EntityChat
import com.karabok.workstep.R

class RecyclerChats(
    private val chats: List<EntityChat>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<RecyclerChats.ChatsAdapter>() {

    class ChatsAdapter(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val nameChat: TextView = itemView.findViewById(R.id.name_chat)
        val dateMessage: TextView = itemView.findViewById(R.id.date_message)
        val textMessage: TextView = itemView.findViewById(R.id.text_message)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsAdapter {
        val itemView: View = LayoutInflater.from(parent.context).
        inflate(R.layout.chat_item, parent, false)

        return ChatsAdapter(itemView, listener)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatsAdapter, position: Int) {
        holder.nameChat.text = chats[position].nameChat
        holder.dateMessage.text = chats[position].dateMessage
        holder.textMessage.text = chats[position].textMessage
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}