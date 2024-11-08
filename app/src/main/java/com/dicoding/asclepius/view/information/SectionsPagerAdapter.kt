package com.dicoding.asclepius.view.information

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.asclepius.view.information.history.HistoryFragment
import com.dicoding.asclepius.view.information.information.InformationFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter (activity) {
    var appName: String = "" // contoh variabel yang bisa diakses dari luar

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InformationFragment()
            1 -> HistoryFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}