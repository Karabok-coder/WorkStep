package com.karabok.workstep.Utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.karabok.workstep.Const.ConstApp
import com.karabok.workstep.R

object FragmentHelper {
    public fun launchFragment(secondFragment: Fragment, transaction: FragmentTransaction) {
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
        transaction.replace(ConstApp.centralFragment, secondFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}