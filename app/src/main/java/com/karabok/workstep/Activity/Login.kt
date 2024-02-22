package com.karabok.workstep.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.karabok.workstep.databinding.ActivityLoginBinding

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

        binding.signIn.setOnClickListener {
            startActivity(intent)
            finish()
//            binding.apply {
//                CoroutineScope(Dispatchers.IO).launch {
//                    if(mail.text.toString() == "" || password.text.toString() == ""){
//                        val errorText: CharSequence = "Введите данные"
//                        runOnUiThread {
//                            passwordLay.error = errorText
//                            mailLay.error = errorText
//                        }
//                        return@launch
//                    }
//
//                    runOnUiThread {
//                        progressLogin.visibility = VISIBLE
//                    }
//
//                    // получение юзера по почте из БД
//                    val user: EntityUsers? = Select.UserByEmail(mail.text.toString())
//
//                    if (user == null){
//                        val errorText: CharSequence = "Не верный"
//                        runOnUiThread {
//                            passwordLay.error = errorText
//                            mailLay.error = errorText
//                            progressLogin.visibility = INVISIBLE
//                        }
//                        return@launch
//                    }
//                    else if (user.password == password.text.toString()){
//                        startActivity(intent)
//                        finish()
//                        runOnUiThread {
//                            progressLogin.visibility = INVISIBLE
//                        }
//                    }
//                    else{
//                        val errorText: CharSequence = "Не верный"
//                        runOnUiThread {
//                            password.error = errorText
//                            mail.error = errorText
//                            progressLogin.visibility = INVISIBLE
//                        }
//                        return@launch
//                    }
//                }
//            }
        }
    }

    private fun initTextFieldListener() {
        binding.passwordLay.setOnClickListener {
            binding.passwordLay.error = ""
        }

        binding.mailLay.setOnClickListener {
            binding.mailLay.error = ""
        }

        binding.password.setOnClickListener {
            binding.passwordLay.error = ""
        }

        binding.email.setOnClickListener {
            binding.mailLay.error = ""
        }

        binding.email.addTextChangedListener{
            binding.mailLay.error = ""
        }

        binding.password.addTextChangedListener{
            binding.passwordLay.error = ""
        }
    }
}