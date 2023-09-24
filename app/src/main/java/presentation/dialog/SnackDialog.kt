package presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tome_aos.databinding.DialogMissionBinding
import com.example.tome_aos.databinding.DialogSnackBinding

class SnackDialog () : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogSnackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSnackBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.closeBtn.setOnClickListener{
            dismiss()
        }

        binding.closeBtnTop.setOnClickListener {
            dismiss()
        }

        return view
    }

}