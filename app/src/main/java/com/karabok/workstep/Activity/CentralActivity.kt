package com.karabok.workstep.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.Fragments.CreateOrders.CreateOrderList1
import com.karabok.workstep.Fragments.HomeFragment
import com.karabok.workstep.Fragments.ProfileFragment
import com.karabok.workstep.Interfaces.OnBottomNavigationChangedListener
import com.karabok.workstep.Interfaces.OnExitAccount
import com.karabok.workstep.R
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.ActivityCentralBinding

class CentralActivity : AppCompatActivity(), OnBottomNavigationChangedListener, OnExitAccount {
    private lateinit var binding: ActivityCentralBinding
    private val centralFragment: Int = R.id.central_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCentralBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        supportFragmentManager
            .beginTransaction()
            .replace(centralFragment, HomeFragment.newInstance())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.orders -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(centralFragment, HomeFragment.newInstance())
                        .commit()
                }

                R.id.new_order -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(centralFragment, CreateOrderList1.newInstance())
                        .commit()
                }

                R.id.profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(centralFragment, ProfileFragment.newInstance())
                        .commit()
                }
            }
            true
        }
    }

    override fun updateBottomNavigation(itemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = itemId
    }

    override fun exitAccont() {
        val sharedPreferences = getSharedPreferences(ConstApp.prefToken, Context.MODE_PRIVATE)
        LoginToken.deleteToken(sharedPreferences)

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

}