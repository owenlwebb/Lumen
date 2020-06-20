package com.example.lumen.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.lumen.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_3,
        R.string.tab_text_4
)

/** Returns a fragment corresponding to one of the tabs. */
class TabsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColorTabFragment.newInstance()
            1 -> PatternsTabFragment.newInstance()
            2 -> AdvancedTabFragment.newInstance()
            else -> ColorTabFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}