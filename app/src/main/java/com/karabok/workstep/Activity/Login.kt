package com.karabok.workstep.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.gson.JsonParser
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.EntityTab.EntityUsers
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationBtn.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()
        }

        // инициализация листенеров для текстовых полей ввода
        initTextFieldListener()

        val intent = Intent(this, CentralActivity::class.java)

        val sharedPreferences = getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

        if(LoginToken.isLogged(sharedPreferences)){
            startActivity(intent)
            finish()
        }

        binding.signIn.setOnClickListener {
            binding.apply {
                CoroutineScope(Dispatchers.IO).launch {
                    if(email.text.toString() == "" || password.text.toString() == ""){
                        val errorText: CharSequence = "Введите данные"
                        runOnUiThread {
                            passwordLay.error = errorText
                            emailLay.error = errorText
                        }
                        return@launch
                    }

                    runOnUiThread {
                        progressLogin.visibility = VISIBLE
                    }

                    var user: EntityUsers? = null

                    launch {
                        val userReq = RequestDbApi.select(ConstAPI.emailUser, email.text.toString())
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
                        }
                    }.join()

                    if (user == null){
                        val errorText: CharSequence = "Не верный"
                        runOnUiThread {
                            passwordLay.error = errorText
                            emailLay.error = errorText
                            progressLogin.visibility = INVISIBLE
                        }
                        return@launch
                    }
                    else if (user?.password != password.text.toString()){
                        val errorText: CharSequence = "Не верный"
                        runOnUiThread {
                            password.error = errorText
                            email.error = errorText
                            progressLogin.visibility = INVISIBLE
                        }
                        return@launch
                    }
                    else if (user?.email != email.text.toString()){
                        val errorText: CharSequence = "Не верный"
                        runOnUiThread {
                            password.error = errorText
                            email.error = errorText
                            progressLogin.visibility = INVISIBLE
                        }
                        return@launch
                    }
                    else{
                        startActivity(intent)
                        finish()
                        runOnUiThread {
                            progressLogin.visibility = INVISIBLE
                        }
                    }

                    val sharedPreferences = getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)

                    if(!LoginToken.isLogged(sharedPreferences)){
                        LoginToken.saveToken("${user?.id}__${user?.email}__${user?.nickname}", sharedPreferences)
                    }
                }
            }
        }
    }

    private fun initTextFieldListener() {
        binding.passwordLay.setOnClickListener {
            binding.passwordLay.error = ""
        }

        binding.emailLay.setOnClickListener {
            binding.emailLay.error = ""
        }

        binding.password.setOnClickListener {
            binding.passwordLay.error = ""
        }

        binding.email.setOnClickListener {
            binding.emailLay.error = ""
        }

        binding.email.addTextChangedListener{
            binding.emailLay.error = ""
        }

        binding.password.addTextChangedListener{
            binding.passwordLay.error = ""
        }
    }
}