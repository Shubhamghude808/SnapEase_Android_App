package com.example.ss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.example.ss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replacefragment(Scanning())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.Scanning -> replacefragment(Scanning())
                R.id.History -> replacefragment(History())

                else ->{ }
            }
            true
        }

    }

    private fun replacefragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.framelayout,fragment)
        fragmentTransaction.commit()
    }
}