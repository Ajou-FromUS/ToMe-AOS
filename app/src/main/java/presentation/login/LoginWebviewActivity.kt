package presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.BuildConfig
import com.example.tome_aos.databinding.AvtivityLoginWebviewBinding
import com.google.gson.GsonBuilder
import data.dto.request.UserRequest
import data.dto.response.JWTTokenResponse
import data.dto.response.UserResponse
import data.service.ApiClient
import data.service.ApiFuroClient
import data.service.LoginService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginWebviewActivity : AppCompatActivity() {
    private lateinit var binding: AvtivityLoginWebviewBinding
    private lateinit var webView: WebView

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
        val userAgentString = webView.settings.userAgentString
        val newUserAgentString = userAgentString.replace("; wv", "").replace("Android ${Build.VERSION.RELEASE};", "")
        webView.settings.userAgentString = newUserAgentString
        webView.loadUrl("$uri")

    }
    inner class LoginWebViewClient(private val context: Context) : WebViewClient() {
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
                //createUser("이재현")
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
                return true
            }
            return true
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
                        println("$accessToken\n $refreshToken")
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
        private fun createUser(nickname: String) {
            val client = ApiClient.getApiClient().create(LoginService::class.java)
            lifecycleScope.launch {
                val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
                val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
                val userRequest = UserRequest(nickname)
                val requestBody = GsonBuilder()
                    .serializeNulls().create()
                    .toJson(userRequest)
                    .toRequestBody("application/json".toMediaTypeOrNull())
                client.createUser(accessToken, refreshToken, requestBody).enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            println("CREATE USER HTTP 성공: ${response.code()}")
                        } else {
                            println("CREATE USER HTTP 오류: ${response.code()}")
                        }
                    }
                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            t.printStackTrace()
                            println("CREATE USER 통신 실패")
                        }
                    })
                }
            }
        }
}




