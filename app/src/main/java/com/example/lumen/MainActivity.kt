package com.example.lumen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.lumen.ui.main.TabsPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup tabs and pager
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)
        val tabsPagerAdapter = TabsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = tabsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}