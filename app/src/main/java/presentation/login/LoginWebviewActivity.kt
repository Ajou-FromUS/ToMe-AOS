package presentation.login

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.tome_aos.databinding.AvtivityLoginWebviewBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

val BASE_URL = "http://www.naver.com"
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
            webViewClient = LoginWebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }
        webView.loadUrl("$uri")
    }
}
class LoginWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        // Redirection을 허용하고 로직을 추가
        val url = request?.url.toString()
        view?.loadUrl(url)
        if (url.contains("?code=") && url != null && url.contains(BASE_URL)){
            val index = url.indexOf("?code=")
            val code = url.substring(index+6)
            sendBody(code)
        }
        return true
    }
    private fun sendBody(code: String){
        val target = "https://api.furo.one/sessions/code/authenticate"
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("code", code)
            .build()
        val request = Request.Builder()
            .url(target)
            .post(requestBody)
            .addHeader("Origin", BASE_URL) // BASE_URL을 헤더에 추가
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val access_token = jsonObject.getString("access_token")
                    val refresh_token = jsonObject.getString("refresh_token")
//                    println("HTTP !@!@@!@!@!@!@!@!@AT!: $access_token")
//                    println("HTTP !@!@!@!@!@!@!@!@!RT: $refresh_token")
                } else {
                    println("HTTP 오류: ${response.code}")
                }
            }
        })
    }
}
