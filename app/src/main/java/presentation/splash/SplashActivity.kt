package presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import presentation.landing.LandingActiviy
import presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            val intent = if (ApplicationClass.getInstance().getDataStore().landingFlag.first()) {
                Intent(this@SplashActivity, LoginActivity::class.java)
            } else {
                Intent(this@SplashActivity, LandingActiviy::class.java)
            }
            moveNext(intent)
        }
    }

    private fun moveNext(intent: Intent){
        binding.ivFaceLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left))
        binding.tvSplash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right))
        binding.ivTextLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right))
        handler.postDelayed({
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, (2000).toLong())
    }
}