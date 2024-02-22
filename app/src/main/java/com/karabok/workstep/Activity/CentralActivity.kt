package com.karabok.workstep.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karabok.workstep.Fragments.ChatsFragment
import com.karabok.workstep.Fragments.HomeFragment
import com.karabok.workstep.R
import com.karabok.workstep.databinding.ActivityCentralBinding

class CentralActivity : AppCompatActivity() {
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
            .replace(R.id.central_fragment, HomeFragment.newInstance())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.ordersRecycler -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(centralFragment, HomeFragment.newInstance())
                        .commit()
                }
                R.id.chats -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(centralFragment, ChatsFragment.newInstance())
                        .commit()
                }
            }
            true
        }
    }

}