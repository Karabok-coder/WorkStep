package com.karabok.workstep.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.EntityTab.EntityChat
import com.karabok.workstep.EntityTab.EntityMessage
import com.karabok.workstep.R
import com.karabok.workstep.Recyclers.RecyclerMessages
import com.karabok.workstep.Utils.FragmentHelper
import com.karabok.workstep.Utils.MessagesHelper
import com.karabok.workstep.databinding.FragmentChatsBinding
import com.karabok.workstep.databinding.FragmentChatsMessagesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatMessagesFragment : Fragment() {
    private lateinit var binding: FragmentChatsMessagesBinding
    private var adapter: RecyclerMessages? = null
    private var dataSet: MutableList<EntityMessage> = mutableListOf()

    companion object {
        @JvmStatic
        fun newInstance() = ChatMessagesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsMessagesBinding.inflate(inflater, container, false)

        val bundle = arguments
        if(bundle != null){
            val chat: EntityChat? = bundle.getParcelable(ConstIntent.entityBundle)
            settingChat(chat)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.backChat.setOnClickListener{
            val bottomNav: View = requireActivity().findViewById(R.id.bottom_navigation)
            bottomNav.visibility = View.VISIBLE
            FragmentHelper.launchFragment(
                ChatsFragment.newInstance(),
                requireActivity().supportFragmentManager.beginTransaction())
            binding.editMessage.clearFocus()
        }
    }

    private fun initChat(){
        if (dataSet.size > 0) {
            binding.recyclerChatMessages.layoutManager = LinearLayoutManager(requireContext())
            adapter = RecyclerMessages(dataSet)
            binding.recyclerChatMessages.adapter = adapter
            binding.recyclerChatMessages.scrollToPosition(0)
        }
    }

    private fun settingChat(chat: EntityChat?) {
        if(chat != null){
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    dataSet = MessagesHelper.getMessages(requireContext(), chat.userId)
                }.join()
            }
            binding.textChatName.text = chat.nameChat
            initChat()
        }
        else{

        }
    }
}