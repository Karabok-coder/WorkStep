package com.karabok.workstep.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.karabok.workstep.R
import com.karabok.workstep.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoMain.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_anim))
        binding.nameMain.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_anim))

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }


//        https://www.google.com/maps/search/?api=1&query=centurylink+field

//        val gmmIntentUri =
//            Uri.parse("https://www.google.ru/maps/place/%D0%90%D0%BB%D0%B5%D0%BA%D1%81%D0%B5%D0%B5%D0%B2%D1%81%D0%BA%D0%B8%D0%B9+%D0%BC%D0%BE%D0%BB%D0%BE%D1%87%D0%BD%D0%BE-%D0%BA%D0%BE%D0%BD%D1%81%D0%B5%D1%80%D0%B2%D0%BD%D1%8B%D0%B9+%D0%9A%D0%BE%D0%BC%D0%B1%D0%B8%D0%BD%D0%B0%D1%82/@50.6153435,38.6872866,17z/data=!4m5!3m4!1s0x41247607cb97236d:0x59255fe78cc52fc7!8m2!3d50.6153435!4d38.6894753/**/")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
//
//        var orders: ArrayList<Orders>? = ArrayList()
//
//        var viewModelJob = Job()
//        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
//
//        uiScope.launch {
//            withContext(Dispatchers.IO) {
//                launch {
//                    orders = Select.allUsers()
//                }
//                withContext(Dispatchers.Main){
//                    if(orders != null){
//                        Log.d(Const.tagLog, orders.toString())
//                    }
//                }
//            }
//        }
}




//val jsonHandler = Duck()
//
//// Пример сериализации объекта в JSON
//val person = EntityTest(25, "John Doe")
//val jsonRepresentation = jsonHandler.serializeToJson(person)
//Luna.log("JSON Representation: $jsonRepresentation")
//
//// Пример десериализации JSON в объект
//val deserializedPerson = jsonHandler.deserializeFromJson(jsonRepresentation)
//Luna.log("Deserialized Person: $deserializedPerson")
//
//val grovy: Grovy = Grovy(this)
//
//grovy.write(jsonRepresentation, "test.json")
//
//val r = grovy.read("test.json")
//
//val deserial = jsonHandler.deserializeFromJson(r)
//
//Luna.log(deserial.toString())




