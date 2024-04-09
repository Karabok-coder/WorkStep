package com.karabok.workstep.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.karabok.workstep.Activity.CentralActivity
import com.karabok.workstep.Activity.Login
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.Const.ConstIntent
import com.karabok.workstep.Fragments.CreateOrders.CreateOrderList2
import com.karabok.workstep.Interfaces.OnBottomNavigationChangedListener
import com.karabok.workstep.Interfaces.OnExitAccount
import com.karabok.workstep.R
import com.karabok.workstep.Utils.LoginToken
import com.karabok.workstep.databinding.FragmentProfileBinding

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

        binding.exitAccout.setOnClickListener {
            listener?.exitAccont()
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

        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}