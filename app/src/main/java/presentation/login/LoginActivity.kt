package presentation.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tome_aos.databinding.ActivityLoginBinding
import com.kakao.sdk.user.UserApiClient
import presentation.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnKakao.setOnClickListener {
            kakaoLogin()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoogle.setOnClickListener {
            var intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.furo.one/oauth?platform=google&public_api_key=apikey-public-live-41fecaee-0924-4e03-80e5-a26a5f3d3788")
            )
            startActivity(intent)
        }
        binding.tvVisitor.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            //이건 추후 방문자 모드로 변경
            startActivity(intent)
        }
    }
    private fun kakaoLogin(){
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null){
                Toast.makeText(this, "token info failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}