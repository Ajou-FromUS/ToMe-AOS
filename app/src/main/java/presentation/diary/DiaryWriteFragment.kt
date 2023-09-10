package presentation.diary

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentDiaryWriteBinding
import presentation.login.LoginActivity

class DiaryWriteFragment : Fragment() {
    private lateinit var binding: FragmentDiaryWriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryWriteBinding.inflate(inflater, container, false)
        val btnSure = binding.btnSure
        val tvMB = binding.tvMessageBox
        val tvSub = binding.tvSubtitle
        val img = binding.ivGood
        val btnNext = binding.ibtnNext
        val tvNext = binding.tvNext

        btnNext.isEnabled = false
        btnSure.setOnClickListener {
            btnSure.visibility = View.INVISIBLE
            img.setImageResource(R.drawable.img_good2)
            tvMB.setText(R.string.start_write)
            tvSub.setText(R.string.good)
            btnNext.setImageResource(R.drawable.ic_diary_frontarr)
            btnNext.isEnabled = true
            tvNext.setTextColor(Color.parseColor("#5072EE"))
        }
//        btnNext.setOnClickListener {
//            val intent = Intent(activity, LoginActivity::class.java)
//            startActivity(intent)
//        }
        return binding.root
    }
}