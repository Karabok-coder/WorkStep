package com.karabok.workstep.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karabok.workstep.Fragments.CodeRegFragment
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.R
import com.karabok.workstep.Utils.Hash
import com.karabok.workstep.Utils.Rand
import com.karabok.workstep.Utils.Requests
import com.karabok.workstep.Utils.Validation.Companion.isDigits
import com.karabok.workstep.Utils.Validation.Companion.isSpecialChars
import com.karabok.workstep.Utils.Validation.Companion.isUpperCases
import com.karabok.workstep.Utils.Validation.Companion.isValidEmail
import com.karabok.workstep.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class Registration : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAcc.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.signUp.setOnClickListener {
            cleanError()
            registration()
        }

        initLayListeners()
    }

    private fun registration() {
        binding.apply {
            progressRegistration.visibility = VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                var isNotEmpty: Boolean = false
                isNotEmpty = errorLayout(onePassword, onePasswordLay) ?: isNotEmpty
                isNotEmpty = errorLayout(twoPassword, twoPasswordLay) ?: isNotEmpty
                isNotEmpty = errorLayout(email, mailLay) ?: isNotEmpty
                isNotEmpty = errorLayout(nickname, nicknameLay) ?: isNotEmpty
                isNotEmpty = errorLayout(firstName, firstNameLay) ?: isNotEmpty

                if(onePassword.text.toString().trim() != twoPassword.text.toString().trim()){
                    runOnUiThread {
                        onePasswordLay.error = "Пароли не совпадают"
                        twoPasswordLay.error = "Пароли не совпадают"
                        progressRegistration.visibility = INVISIBLE
                    }
                    return@launch
                }
                if(onePassword.text.toString().trim().length < 10){
                    runOnUiThread {
                        onePasswordLay.error = "Пароль должен содержать больше 10 символов"
                        twoPasswordLay.error = "Пароль должен содержать больше 10 символов"
                        progressRegistration.visibility = INVISIBLE
                    }
                    return@launch
                }
                if(!email.text.toString().trim().isValidEmail()){
                    runOnUiThread {
                        mailLay.error = "Электронная почта введена не верно"
                        progressRegistration.visibility = INVISIBLE
                    }
                    return@launch
                }

                var resp = ""
                resp = RequestDbApi.select(
                    ConstAPI.existUserNickname,
                    JSONObject()
                        .put("nickname", nickname.text.toString().trim())
                        .toString()
                )

                if(resp.toInt() > 0) {
                    runOnUiThread {
                        nicknameLay.error = "Такой никнейм уже существует"
                        progressRegistration.visibility = INVISIBLE
                    }
                    return@launch
                }

                resp = RequestDbApi.select(
                    ConstAPI.existUserNickname,
                    JSONObject()
                        .put("nickname", nickname.text.toString().trim()).toString()
                )

                if(resp.toInt() > 0) {
                    runOnUiThread {
                        mailLay.error = "Такая почта уже зарегистрированна"
                        progressRegistration.visibility = INVISIBLE
                    }
                    return@launch
                }

                if (isNotEmpty) return@launch

                val randSeed: Long = Rand().nextInt()
                val hashPassword: String = Hash.sha256(onePassword.text.toString())

                launch {
                    Requests.post(
                        ConstAPI.domainLink + ConstAPI.emailSend,
                        JSONObject()
                            .put("seed", randSeed.toInt())
                            .put("username", firstName.text.toString().trim())
                            .put("email", email.text.toString().trim().lowercase())
                            .toString())
                }.join()

                val codeFragment = CodeRegFragment()
                val bundle: Bundle = Bundle().apply {
                    putLong(ConstIntent.randSeed, randSeed)
                    putString(ConstIntent.email, email.text.toString().trim().lowercase())
                    putString(ConstIntent.password, hashPassword)
                    putString(ConstIntent.nickname, nickname.text.toString().trim())
                    putString(ConstIntent.firstName, firstName.text.toString().trim())
                }
                codeFragment.arguments = bundle

                runOnUiThread{
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.code_fragment_layout, codeFragment)
                        commit()
                        regPanel.visibility = GONE
                        progressRegistration.visibility = INVISIBLE
                        codeFragmentLayout.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun errorLayout(textInput: TextInputEditText, textInputLayout: TextInputLayout): Boolean? {
        if (textInput.text.toString().trim() == "") {
            runOnUiThread {
                textInputLayout.error = "Необходимо заполнить"
            }
            return true
        }
        return null
    }

    private fun cleanError(){
        binding.apply {
            onePasswordLay.setOnClickListener{
                onePasswordLay.error = ""
            }
            twoPasswordLay.setOnClickListener{
                twoPasswordLay.error = ""
            }
            firstNameLay.setOnClickListener{
                firstNameLay.error = ""
            }
            nicknameLay.setOnClickListener{
                nicknameLay.error = ""
            }
            mailLay.setOnClickListener{
                mailLay.error = ""
            }
        }
    }

    private fun initLayListeners(){
        binding.apply {
            cleanError()

            changeTextListeners(onePassword)
            changeTextListeners(twoPassword)
            changeTextListeners(email)
            changeTextListeners(firstName)
            changeTextListeners(nickname)

            onePassword.addTextChangedListener {
                if(onePassword.text.toString().trim().length >= 10){
                    val point: Int = determiningStrength()
                    binding.strengthPassword.progress = point
                    binding.textStrengthPassword.text = "Сложность пароля: $point"
                }
            }
        }
    }

    private fun changeTextListeners(textInput: TextInputEditText){
        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val textWithoutSpaces = s?.toString()?.replace(" ", "") ?: ""

                if (textWithoutSpaces != s?.toString()) {
                    textInput.setText(textWithoutSpaces)
                    textInput.setSelection(textWithoutSpaces.length)
                }
            }
        })
    }

    private fun determiningStrength(): Int {
        var pointStrength: Int = 20
        binding.apply {
            val passwordText: String = onePassword.text.toString()

            if(passwordText.length > 10){
                pointStrength += 20
            }
            if(passwordText.isUpperCases()){
                pointStrength += 20
            }
            if(passwordText.isSpecialChars()){
                pointStrength += 20
            }
            if(passwordText.isDigits()){
                pointStrength += 20
            }
        }
        return pointStrength
    }
}