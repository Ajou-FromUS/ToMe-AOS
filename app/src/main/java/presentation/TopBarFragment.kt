package presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.DialogMissionBinding
import com.example.tome_aos.databinding.FragmentTopBarBinding
import presentation.dialog.MissionClass


class TopBarFragment : Fragment() {

    private lateinit var binding: FragmentTopBarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setMissionClickEvent()
    }

    private fun setMissionClickEvent() {
        binding.missionBtn.setOnClickListener {
            Log.d("test log","미션버튼");

            val dialog = MissionClass()
            dialog.show(activity?.supportFragmentManager!!, "MissionDialog")

        }
    }

}