package com.example.lumen.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.lumen.R


class AdvancedTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.advanced_tab_fragment, container, false)
    }
    companion object {
        @JvmStatic
        fun newInstance(): AdvancedTabFragment { return AdvancedTabFragment() }
    }

}
