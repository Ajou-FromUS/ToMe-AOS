package presentation.landing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.databinding.FragmentFifthBinding
import presentation.login.LoginActivity

class FifthFragment : Fragment() {
    private lateinit var binding: FragmentFifthBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFifthBinding.inflate(inflater, container, false)
        binding.btnStartTome.setOnClickListener{
            var intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}