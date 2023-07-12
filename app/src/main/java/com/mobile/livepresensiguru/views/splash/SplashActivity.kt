package com.mobile.livepresensiguru.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mobile.livepresensiguru.R
import com.mobile.livepresensiguru.hawkstorage.HawkStorage
import com.mobile.livepresensiguru.views.login.LoginActivity
import com.mobile.livepresensiguru.views.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        afterDelayGotoLogin()
    }

    private fun afterDelayGotoLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkIsLogin()
        },1200)
    }

    private fun checkIsLogin() {
        val isLogin = HawkStorage.instance(this).isLogin()
        if (isLogin){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

}
