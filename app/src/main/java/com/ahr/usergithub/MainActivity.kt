package com.ahr.usergithub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahr.usergithub.database.UserHelper
import com.ahr.usergithub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}