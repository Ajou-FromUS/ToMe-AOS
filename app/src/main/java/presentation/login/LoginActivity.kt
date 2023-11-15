package presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tome_aos.BuildConfig
import com.example.tome_aos.databinding.ActivityLoginBinding
import presentation.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnKakao.setOnClickListener {
            var uri = BuildConfig.KAKAO_URI
            webviewLogin(uri)
        }
        binding.btnGoogle.setOnClickListener {
            var uri = BuildConfig.GOOGLE_URI
            webviewLogin(uri)
        }
        binding.tvVisitor.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            //이건 추후 방문자 모드로 변경
            startActivity(intent)
        }
    }

    private fun webviewLogin(uri: String) {
        val intent = Intent(this, LoginWebviewActivity::class.java)
        intent.putExtra("uri", uri)
        startActivity(intent)
    }
}