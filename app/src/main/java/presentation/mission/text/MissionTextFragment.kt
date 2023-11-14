package presentation.mission.text

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionTextBinding
import presentation.mission.MissionCompleteFragment

class MissionTextFragment : Fragment() {
    private lateinit var binding: FragmentMissionTextBinding
    private lateinit var et: EditText
    private lateinit var showBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMissionTextBinding.inflate(inflater, container, false).apply {
            et = missionTextField
            showBtn = showToTextBtn
            backBtn = backTextBtn
        }

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
            val MissionCompleteFragment = MissionCompleteFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frameLayout, MissionCompleteFragment)
                //.addToBackStack("archiveWrite")
                .commit()
        }
        backBtn.setOnClickListener {
            showKeyboard()
        }
        return binding.root
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