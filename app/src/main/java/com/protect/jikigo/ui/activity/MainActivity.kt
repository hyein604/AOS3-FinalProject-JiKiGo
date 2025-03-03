package com.protect.jikigo.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.protect.jikigo.R
import com.protect.jikigo.databinding.ActivityMainBinding
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.statusBarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLayout()
    }

    private fun setLayout() {
        splashPlayer()
    }

    private fun splashPlayer() {
        this.statusBarColor(R.color.primary)

        lifecycleScope.launch {
            delay(3000)
            keepSplashScreen = false
            checkAutoLogin()
        }
    }

    private fun checkAutoLogin() {
        lifecycleScope.launch {
            val userId = this@MainActivity.getUserId()
            val intent = if (!userId.isNullOrEmpty()) {
                Intent(this@MainActivity, HomeActivity::class.java)
            } else {
                Intent(this@MainActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}