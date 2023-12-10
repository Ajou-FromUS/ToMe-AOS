package presentation.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tome_aos.databinding.ActivitySignupBinding

class SignupActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}