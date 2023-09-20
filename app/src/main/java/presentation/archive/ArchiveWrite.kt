package presentation.archive

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentArchiveWriteBinding

class ArchiveWrite : Fragment() {
    private lateinit var binding: FragmentArchiveWriteBinding
    private lateinit var et: EditText
    private lateinit var tv: TextView
    private lateinit var btn: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveWriteBinding.inflate(inflater, container, false).apply {
            et = etArchive
            tv = tvWordCount
            btn = btnSaveArchive
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

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        btn.setOnClickListener {
            val textInput = et.text.toString()
            val fragment = ArchiveSave(textInput)
            parentFragmentManager.beginTransaction()
                .add(R.id.frame_archiveWrite, fragment)
                //.hide(this@ArchiveWrite)

                .addToBackStack("archiveWrite")
                .commit()
        }
        return binding.root
    }

    private fun updateButtonUI() {
        var textInput = et.text.toString()
        tv.text = textInput.length.toString() + "/80자"
        if (textInput.isEmpty()) {
            btn.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.color_disabled1))
            btn.setTextColor(getColor(requireContext(), R.color.color_font3))
            btn.isEnabled = false
        } else {
            btn.setBackgroundResource(R.drawable.btn_basic_8dp)
            btn.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.color_main))
            btn.setTextColor(getColor(requireContext(), R.color.color_font4))
            btn.isEnabled = true
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(et.windowToken, 0)
        et.clearFocus()
    }
}