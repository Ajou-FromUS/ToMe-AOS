package presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivitySplashBinding
import presentation.landing.LandingActiviy

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveNext()
    }

    private fun moveNext(){
        binding.ivFaceLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left))
        binding.tvSplash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right))
        binding.ivTextLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right))
        handler.postDelayed({
            val intent = Intent(this, LandingActiviy::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, (2000).toLong())
    }
}