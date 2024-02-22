package com.karabok.workstep.Fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.EntityTab.EntityChat
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.R
import com.karabok.workstep.Recyclers.RecyclerChats
import com.karabok.workstep.Utils.FragmentHelper
import com.karabok.workstep.Utils.MessagesHelper
import com.karabok.workstep.Utils.Requests
import com.karabok.workstep.databinding.FragmentChatsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatsFragment : Fragment(), RecyclerChats.OnItemClickListener {
    private lateinit var binding: FragmentChatsBinding
    public var adapter: RecyclerChats? = null
    public var chats: MutableList<EntityChat> = mutableListOf()

    var chatInstance: ChatMessagesFragment = ChatMessagesFragment.newInstance()

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        initChats()
    }

    private fun initChats() = binding.apply {
        CoroutineScope(Dispatchers.IO).launch {
            var response: String = ""
            var jsonObject: JsonObject = JsonObject()

            launch {
                response = Requests.get(ConstAPI.domainLink + ConstAPI.selectFriends + "?userId=80")
                jsonObject = JsonParser.parseString(response).asJsonObject.get("content").asJsonObject
            }.join()

            Luna.log(response)

            for ((key, value) in jsonObject.entrySet()) {
                val chatsJson = jsonObject.get(key).asJsonObject
                val messages = MessagesHelper.getMessages(requireContext(), chatsJson["userId"].asInt)
                if(messages.size > 0) {
                    chats.add(
                        EntityChat(
                            chatsJson["userId"].asInt,
                            chatsJson["firstName"].asString,
                            messages[messages.size - 1].messageDate,
                            messages[messages.size - 1].messageText
                        )
                    )
                }
                else {
                    chats.add(
                        EntityChat(
                            chatsJson["userId"].asInt,
                            chatsJson["firstName"].asString,
                            "",
                            "Пока нет сообщений"
                        )
                    )
                }
            }

            withContext(Dispatchers.Main) {
                if(chats.size > 0) {
                    binding.textEmptyChats.visibility = View.GONE
                    binding.chatsList.visibility = View.VISIBLE
                    binding.chatsList.layoutManager = LinearLayoutManager(requireContext())
                    adapter = RecyclerChats(chats, this@ChatsFragment)
                    binding.chatsList.adapter = adapter
                    binding.chatsList.scrollToPosition(0)
                }
                else{
                    binding.textEmptyChats.visibility = View.VISIBLE
                    binding.chatsList.visibility = View.GONE
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        val chatFragment = ChatMessagesFragment.newInstance()

        val bundle: Bundle = Bundle().apply {
            putParcelable(ConstIntent.entityBundle, chats[position])
        }
        chatFragment.arguments = bundle

        FragmentHelper.launchFragment(
            chatFragment,
            requireActivity().supportFragmentManager.beginTransaction())

        val bottomNav: View = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
    }
}