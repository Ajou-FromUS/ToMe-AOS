package presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import application.ApplicationClass
import com.example.tome_aos.BuildConfig
import com.example.tome_aos.databinding.AvtivityLoginWebviewBinding
import data.dto.response.JWTTokenResponse
import data.service.ApiFuroClient
import data.service.LoginService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import presentation.MainActivity
import presentation.chat.ChatActivity
import retrofit2.Call
import retrofit2.Response

class LoginWebviewActivity : AppCompatActivity() {
    private lateinit var binding: AvtivityLoginWebviewBinding
    private lateinit var webView: WebView

    //구글 로그인 때문에 chrome custom tab으로 바꿔야 함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AvtivityLoginWebviewBinding.inflate(layoutInflater)
        webView = binding.wvLogin
        setContentView(binding.root)

        val uri = intent.getStringExtra("uri")
        webView.apply {
            webViewClient = LoginWebViewClient(this@LoginWebviewActivity)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }
        webView.loadUrl("$uri")
    }
}

class LoginWebViewClient(private val context: Context) : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?,
    ): Boolean {
        val url = request?.url.toString()
        view?.loadUrl(url)
        if (url.contains("?code=") && url != null && url.contains(BuildConfig.REDIRECT_URL)) {
            val index = url.indexOf("?code=")
            val code = url.substring(index + 6)
            sendBody(code)
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finish()
            return true
        }
        return true
    }
}

private fun sendBody(code: String) {
    val client = ApiFuroClient.getApiClient().create(LoginService::class.java)
    val requestBody = FormBody.Builder()
        .add("code", code)
        .build()
    val call = client.sendCode(requestBody)
    call.enqueue(object : retrofit2.Callback<JWTTokenResponse> {
        override fun onResponse(call: Call<JWTTokenResponse>, response: Response<JWTTokenResponse>) {
            if (response.isSuccessful) {
                val jwtTokenResponse = response.body()
                val accessToken = jwtTokenResponse?.accessToken ?: ""
                val refreshToken = jwtTokenResponse?.refreshToken ?: ""
                //token 저장
                CoroutineScope(Dispatchers.Main).launch {
                    ApplicationClass.getInstance().getDataStore().saveTokens(accessToken, refreshToken)
                }
            } else { println("HTTP 오류: ${response.code()}") }
        }
        override fun onFailure(call: Call<JWTTokenResponse>, t: Throwable) {
            t.printStackTrace()
        }
    })
}

