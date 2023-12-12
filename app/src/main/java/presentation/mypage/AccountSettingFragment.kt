package presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.databinding.FragmentAccountSettingBinding
import com.google.gson.GsonBuilder
import data.dto.request.UserRequest
import data.service.ApiClient
import data.service.MyPageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.login.LoginActivity

class AccountSettingFragment: Fragment() {
    private lateinit var binding: FragmentAccountSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var filterWords = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val ps = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$".toRegex()
            if (source.isEmpty() || ps.matches(source)) {
                source
            } else {
                Toast.makeText(requireContext(), "한글, 영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                ""
            }
        })
        binding = FragmentAccountSettingBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            var nicknames = ApplicationClass.getInstance().getDataStore().nickname.first()
//            println(nicknames)
            binding.etNickname.hint = nicknames
        }
        binding.etNickname.filters = filterWords
        //닉네임 불러온 값 여기에 저장
        btnListener()
        return binding.root
    }
    private fun btnListener() {
        val fragmentManager = parentFragmentManager
        //뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            fragmentManager.popBackStack()
        }
        //editText 공백 미허용
        binding.etNickname.setOnFocusChangeListener { _, hasFocus ->
            binding.btnNicknameModify.isEnabled = !hasFocus && binding.etNickname.text.isNotBlank()
        }
        //닉네임 수정
        binding.btnNicknameModify.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            changeNickname(nickname)
        }
        //로그아웃
        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Token remove
                ApplicationClass.getInstance().getDataStore().removeTokens()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        //회원탈퇴
        binding.btnResign.setOnClickListener {

        }
    }
    private fun changeNickname(nickname: String) {
        val client = ApiClient.getApiClient().create(MyPageService::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val userRequest = UserRequest(nickname)
            val requestBody = GsonBuilder()
                .serializeNulls().create()
                .toJson(userRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            println(nickname)
            try {
                val response = client.changeNickname(accessToken, refreshToken, requestBody)
                binding.etNickname.hint = response.data.nickname
                binding.etNickname.text.clear()
                ApplicationClass.getInstance().getDataStore().saveNickname(response.data.nickname)
//                println("성공")
                Toast.makeText(requireContext(),"닉네임 변경 성공. 앱을 다시 시작해주세요.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(),"요청 실패. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}