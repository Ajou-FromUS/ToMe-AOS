package presentation.login

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tome_aos.databinding.AvtivityLoginWebviewBinding

class LoginWebviewActivity: AppCompatActivity() {
    private lateinit var binding: AvtivityLoginWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding = AvtivityLoginWebviewBinding.inflate(layoutInflater)
        val webView = binding.wvLogin
        if(intent.hasExtra("uri")) {
            val uri = intent.getStringExtra("uri")
            webView.apply{
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
            webView.loadUrl(uri)
        }else{
            Toast.makeText(this, "")
        }


    }
}