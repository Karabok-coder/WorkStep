package com.karabok.workstep.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonParser
import com.karabok.workstep.Activity.CentralActivity
import com.karabok.workstep.Activity.Login
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityProfile
import com.karabok.workstep.EntityTab.EntityUsers
import com.karabok.workstep.Fragments.CreateOrders.CreateOrderList2
import com.karabok.workstep.Interfaces.OnBottomNavigationChangedListener
import com.karabok.workstep.Interfaces.OnExitAccount
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.R
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var listener: OnExitAccount? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return  binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnExitAccount) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnBottomNavigationChangedListener")
        }
    }

    override fun onStart() {
        super.onStart()

        binding.progressProfile.visibility = VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                val sharedPreferences =
                    activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

                val token = LoginToken.getToken(sharedPreferences)
                val nickname = token?.split("__")?.get(2)

                var user: EntityUsers? = null

                val userReq = RequestDbApi.select(
                    ConstAPI.selectNicknameUser,
                    "nickname=$nickname"
                )

                var jsonObject = JsonParser.parseString(userReq).asJsonObject.get("content").asJsonObject
                for ((key, value) in jsonObject.entrySet()) {
                    val userJson = jsonObject.get(key).asJsonObject
                    user = EntityUsers(
                        userJson["id"].toString().toInt(),
                        userJson["email"].toString().trim('"'),
                        userJson["password"].toString().trim('"'),
                        userJson["nickname"].toString().trim('"'),
                        userJson["dateReg"].toString().trim('"')
                    )
                }

                var profile: EntityProfile? = null

                val profileReq = RequestDbApi.select(
                    ConstAPI.selectProfileUserId,
                    "userId=${user?.id}"
                )

                jsonObject = JsonParser.parseString(profileReq).asJsonObject.get("content").asJsonObject
                for ((key, value) in jsonObject.entrySet()) {
                    val userJson = jsonObject.get(key).asJsonObject
                    profile = EntityProfile(
                        userJson["id"].toString().toInt(),
                        userJson["userId"].toString().toInt(),
                        userJson["firstName"].toString().trim('"')
                    )
                }

                requireActivity().runOnUiThread {
                    binding.nameProfile.text = "Имя: ${profile?.firstName}"
                    binding.nicknameProfile.text = "Никнейм: ${user?.nickname}"
                    binding.emailProfile.text = "Почта: ${user?.email}"
                    binding.progressProfile.visibility = INVISIBLE
                }

            }.join()
        }

        binding.exitAccout.setOnClickListener {
            listener?.exitAccont()
        }

        binding.myOrders.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = MyOrdersFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.editNickname.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = EditNicknameFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.editPassword.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val newFragment = EditPasswordFragment.newInstance()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.central_fragment, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}