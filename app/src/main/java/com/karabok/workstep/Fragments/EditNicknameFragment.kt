package com.karabok.workstep.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityUsers
import com.karabok.workstep.Loguru.Luna
import com.karabok.workstep.Utils.Hash
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.Utils.Requests
import com.karabok.workstep.databinding.FragmentEditNicknameBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditNicknameFragment : Fragment() {
    private lateinit var binding: FragmentEditNicknameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNicknameBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditNicknameFragment()
    }

    override fun onStart() {
        super.onStart()

        binding.saveEditNickname.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    val sharedPreferences = activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

                    val token = LoginToken.getToken(sharedPreferences)
                    val nickname = token?.split("__")?.get(2)

                    var user: EntityUsers? = null

                    val userReq = RequestDbApi.select(
                        ConstAPI.selectNicknameUser,
                        "nickname=$nickname"
                    )

                    Luna.log(userReq)

                    val jsonObject = JsonParser.parseString(userReq).asJsonObject.get("content").asJsonObject
                    for ((key, value) in jsonObject.entrySet()) {
                        val userJson = jsonObject.get(key).asJsonObject
                        user = EntityUsers(
                            userJson["id"].toString().toInt(),
                            userJson["email"].toString().trim('"'),
                            userJson["password"].toString().trim('"'),
                            userJson["nickname"].toString().trim('"'),
                            userJson["dateReg"].toString().trim('"')
                        )

                        Luna.log(user.toString())
                    }

                    if (user?.password != Hash.sha256(binding.passwordEditNickname.text.toString())){
                        requireActivity().runOnUiThread {
                            binding.passwordEditNickname.error = "Не правильный пароль"
                        }
                    }
                    else{
                        val exist = Requests.post(
                            ConstAPI.domainLink + ConstAPI.existUserNickname,
                            JSONObject()
                                .put("nickname", binding.newNickname.text.toString())
                                .toString()
                        )

                        if (exist.toBoolean()){
                            requireActivity().runOnUiThread {
                                binding.newNickname.error = "Ник занят"
                            }
                        }
                        else{
                            RequestDbApi.update(
                                ConstAPI.updateNicknameUser,
                                JSONObject()
                                    .put("nickname", binding.newNickname.text.toString())
                                    .put("id", user.id)
                                    .toString()
                            )

                            LoginToken.setToken("${user.id}__${user.email}__${binding.newNickname.text.toString()}", sharedPreferences)
                        }
                    }
                }.join()
            }
        }


    }


}
