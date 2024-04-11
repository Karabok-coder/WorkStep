package com.karabok.workstep.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityUsers
import com.karabok.workstep.R
import com.karabok.workstep.Utils.Hash
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.FragmentEditPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditPasswordFragment : Fragment() {
    private lateinit var binding: FragmentEditPasswordBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditPasswordFragment()
    }

    override fun onStart() {
        super.onStart()
        binding.saveEditPassword.setOnClickListener { binding.apply {
            if (newPassword.text.toString() == newPasswordRe.text.toString()){
                CoroutineScope(Dispatchers.IO).launch { launch {
                    requireActivity().runOnUiThread {
                        progressEditPassword.visibility = View.VISIBLE
                    }

                    val sharedPreferences = activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

                    val token = LoginToken.getToken(sharedPreferences)
                    val nickname = token?.split("__")?.get(2)

                    var user: EntityUsers? = null

                    val userReq = RequestDbApi.select(
                        ConstAPI.selectNicknameUser,
                        "nickname=$nickname"
                    )

                    val jsonObject =
                        JsonParser.parseString(userReq).asJsonObject.get("content").asJsonObject
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

                    if (user?.password != Hash.sha256(passwordEditPassword.text.toString())){
                        val errorText: CharSequence = "Не верный"
                        requireActivity().runOnUiThread {
                            passwordEditPassword.error = errorText
                            progressEditPassword.visibility = View.INVISIBLE
                        }
                    }
                    else{
                        RequestDbApi.update(
                            ConstAPI.updatePasswordUser,
                            JSONObject()
                                .put("password", Hash.sha256(newPassword.text.toString()))
                                .put("id", user.id)
                                .toString()
                        )
                        requireActivity().runOnUiThread{
                            Toast.makeText(activity?.applicationContext, "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                            progressEditPassword.visibility = View.INVISIBLE

                            val fragmentManager = activity?.supportFragmentManager
                            val newFragment = ProfileFragment.newInstance()
                            val transaction = fragmentManager?.beginTransaction()
                            transaction?.replace(R.id.central_fragment, newFragment)
                            transaction?.commit()
                        }
                    }
                }.join()}
            }
            else{
                Toast.makeText(activity?.applicationContext, "Новые пароли не совпадают", Toast.LENGTH_SHORT).show();
                progressEditPassword.visibility = View.INVISIBLE
            }
        }}

    }
}