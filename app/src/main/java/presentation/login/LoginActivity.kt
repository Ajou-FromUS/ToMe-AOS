package presentation.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tome_aos.BuildConfig
import com.example.tome_aos.databinding.ActivityLoginBinding
import presentation.MainActivity
import presentation.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isLoginSuccessful = false
    private val startWebViewActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            isLoginSuccessful = data?.getBooleanExtra("loginSuccess", false) ?: false
            if (isLoginSuccessful) {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
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
        startWebViewActivity.launch(intent)
    }
}