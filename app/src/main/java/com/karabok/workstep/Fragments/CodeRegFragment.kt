package com.karabok.workstep.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonParser
import com.karabok.workstep.Activity.CentralActivity
import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.DbApi.RequestDbApi
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.Utils.Rand
import com.karabok.workstep.Utils.TimeUtil
import com.karabok.workstep.databinding.FragmentCodeRegBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CodeRegFragment : Fragment() {
    private lateinit var binding: FragmentCodeRegBinding

    private var randSeed: Long? = null
    private var password: String? = null
    private var email: String? = null
    private var nickname: String? = null
    private var firstName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeRegBinding.inflate(inflater, container, false)
        val dataBundle = arguments
        if (dataBundle != null) {
            randSeed = dataBundle.getLong(ConstIntent.randSeed)
            email = dataBundle.getString(ConstIntent.email)
            password = dataBundle.getString(ConstIntent.password)
            nickname = dataBundle.getString(ConstIntent.nickname)
            firstName = dataBundle.getString(ConstIntent.firstName);
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            onTextChangeListener(code1, code2)
            onTextChangeListener(code2, code3)
            onTextChangeListener(code3, code4)
            onTextChangeListener(code4, code5)
            onTextChangeListener(code5, code6)

            onTextDeleteListener(code2, code1)
            onTextDeleteListener(code3, code2)
            onTextDeleteListener(code4, code3)
            onTextDeleteListener(code5, code4)

            settingTextEdit(code1)
            settingTextEdit(code2)
            settingTextEdit(code3)
            settingTextEdit(code4)
            settingTextEdit(code5)
            settingTextEdit(code6)

            clearCode.setOnClickListener {
                cleanCode()
            }

            code6.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && code6.text.toString() != "") {
                    code6.text?.clear()
                    code6.requestFocus()
                    return@setOnKeyListener true
                }
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    code5.text?.clear()
                    code5.requestFocus()
                    return@setOnKeyListener true
                }
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    applyCode(getCodeInTextInput())
                }
                return@setOnKeyListener false
            }

            code6.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    errorInputCode.text = ""
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(code6.text.toString().length > 1){
                        code6.setText(code6.text.toString()[0].toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }


    private fun applyCode(code: String){
        var genCode: String = ""
        if (randSeed != null){
            genCode = Rand(randSeed!!.toLong()).nextInt(100000, 999999).toString()
        }
        else
        {
            binding.errorInputCode.text = "Произошла ошибка, попробуйте заного через некоторое время"
            return
        }

        binding.progressCode.visibility = VISIBLE

        if(code == genCode) {
            val intent = Intent(activity, CentralActivity::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    // Insert new User
                    val insertJson: String = RequestDbApi.insert(
                        ConstAPI.insertUser,
                        JSONObject()
                            .put("email", email)
                            .put("password", password)
                            .put("nickname", nickname)
                            .put("dateReg", TimeUtil.getMoscowTime().toString())
                            .toString()
                    )

                    val userInsertId = JsonParser.parseString(insertJson).asJsonObject.get("val").asInt

                    // Insert new Profile
                    RequestDbApi.insert(
                        ConstAPI.insertProfile,
                        JSONObject()
                            .put("userId", userInsertId)
                            .put("firstName", firstName)
                            .toString()
                    )

                    val sharedPreferences = activity?.getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)
                    LoginToken.saveToken("${userInsertId}__${email}__${nickname}", sharedPreferences)
                }.join()
            }
            binding.progressCode.visibility = INVISIBLE
            startActivity(intent)
        }
        else {
            cleanCode()
            binding.errorInputCode.text = "Не верный код"
            binding.progressCode.visibility = INVISIBLE
            return
        }
        return
    }

    private fun onTextDeleteListener(currentTextInput: TextInputEditText, backTextInput: TextInputEditText) {
        currentTextInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                backTextInput.text?.clear()
                backTextInput.requestFocus()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun settingTextEdit(currentTextInput: TextInputEditText) {
        currentTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.errorInputCode.text = ""
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(currentTextInput.text.toString().length > 1){
                    currentTextInput.setText(currentTextInput.text.toString()[0].toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun onTextChangeListener(currentTextInput: TextInputEditText, nextTextInput: TextInputEditText){
        currentTextInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                nextTextInput.requestFocus()
            }
        })
    }

    fun getCodeInTextInput(): String{
        var fullCode: String = ""
        binding.apply {
            fullCode += code1.text.toString() +
                    code2.text.toString() +
                    code3.text.toString() +
                    code4.text.toString() +
                    code5.text.toString() +
                    code6.text.toString()
        }
        return fullCode
    }

    fun cleanCode() {
        val editable = Editable.Factory.getInstance().newEditable("")
        binding.apply {
            code1.text = editable
            code2.text = editable
            code3.text = editable
            code4.text = editable
            code5.text = editable
            code6.text = editable
            code1.requestFocus()
            binding.errorInputCode.text = ""
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CodeRegFragment()
    }
}