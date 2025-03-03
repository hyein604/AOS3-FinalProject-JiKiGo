package com.protect.jikigo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.ActivityMainBinding
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.statusBarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
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

        Glide.with(this)
            .load(R.drawable.splash_video)
            .into(binding.ivSplash)

        lifecycleScope.launch {
            delay(3000)
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