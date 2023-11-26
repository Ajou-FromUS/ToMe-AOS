package presentation.mission.text

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionTextBinding
import com.google.gson.GsonBuilder
import data.dto.request.MissionCompleteRequest
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.MissionCompleteService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.mission.MissionCompleteFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MissionTextFragment : Fragment() {
    private lateinit var binding: FragmentMissionTextBinding
    private lateinit var titleTextfield: TextView
    private lateinit var et: EditText
    private lateinit var showBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionTextBinding.inflate(inflater, container, false).apply {
            titleTextfield = titleText
            et = missionTextField
            showBtn = showToTextBtn
            backBtn = backTextBtn
        }

        var missionDetail = arguments?.getString("missionTitle")
        var missionID = arguments?.getInt("missionID")
        titleTextfield.text = missionDetail

        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonUI()
            }

            override fun afterTextChanged(s: Editable?) {
                updateButtonUI()
            }
        })
        et.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(et.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }


        showBtn.setOnClickListener {
            val textInput = et.text.toString()
            patchTextMission(textInput, missionID)
        }
        backBtn.setOnClickListener {
            showKeyboard()
        }
        return binding.root
    }

    private fun completePage(){
        val missionCompleteFragment = MissionCompleteFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frameLayout, missionCompleteFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }

    private fun patchTextMission(content: String?, missionID: Int?) {
        val client = ApiClient.getApiClient().create(MissionCompleteService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val missionCompleteRequest = MissionCompleteRequest(content)
            val requestBody = GsonBuilder()
                .serializeNulls().create()
                .toJson(missionCompleteRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            client.getMissions(accessToken, refreshToken, missionID, requestBody).enqueue(object : Callback<MissionResponse.Data> {
                override fun onResponse(call: Call<MissionResponse.Data>, response: Response<MissionResponse.Data>) {
                    if (response.isSuccessful) {
                        completePage()
                    } else {
                        println("HTTP 오류: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<MissionResponse.Data>, t: Throwable) {
                    Log.d("fail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }

    private fun updateButtonUI() {
        var textInput = et.text.toString()
        if (textInput.isEmpty()) {
            showBtn.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_disabled1
                )
            )
            showBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_font3))
            showBtn.isEnabled = false
        } else {
            showBtn.setBackgroundResource(R.drawable.btn_basic_8dp)
            showBtn.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_main
                )
            )
            showBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_font4))
            showBtn.isEnabled = true
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(et.windowToken, 0)
        et.clearFocus()
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.showSoftInput(et, 0)
    }


}