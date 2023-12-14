package presentation.signup

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivitySignupBinding
import com.google.gson.GsonBuilder
import data.dto.request.UserRequest
import data.dto.response.UserResponse
import data.service.ApiClient
import data.service.LoginService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.CommonTextWatcher
import util.hideKeyboard

class SignupActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var nick = false
    private var birth = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        signup()
        binding.etNickname.addTextChangedListener(CommonTextWatcher(
            onChanged = { source, _, _, _ ->
                nick = !source.isNullOrEmpty()
                checkCondition()
            }
        ))
        binding.etBirthdate.addTextChangedListener(CommonTextWatcher(
            onChanged = { source, _, _, _ ->
                birth = !source.isNullOrEmpty()
                checkCondition()
            }
        ))
    }
    private fun checkCondition() {
        if (nick && birth) {
            binding.btnSignUp.setBackgroundResource(R.drawable.btn_basic_8dp)
            binding.btnSignUp.setTextColor(ContextCompat.getColorStateList(this, R.color.color_font4))
            binding.btnSignUp.isEnabled = true
            binding.btnSignUp.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_main)
        } else {
            binding.btnSignUp.setBackgroundResource(R.drawable.btn_basic_8dp_noripple)
            binding.btnSignUp.setTextColor(ContextCompat.getColorStateList(this, R.color.color_font3))
            binding.btnSignUp.isEnabled = false
            binding.btnSignUp.backgroundTintList = ContextCompat.getColorStateList(this, R.color.color_disabled1)
        }
    }
    private fun signup() {
        binding.btnSignUp.setOnClickListener {
            createUser(binding.etNickname.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                hideKeyboard(focusView)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}