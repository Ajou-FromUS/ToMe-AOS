package presentation.archive

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentArchiveBinding
import com.example.tome_aos.databinding.FragmentArchiveSaveBinding

class ArchiveSave(private val textInput: String) : Fragment() {
    private lateinit var binding: FragmentArchiveSaveBinding
    private lateinit var bindingArchive: FragmentArchiveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveSaveBinding.inflate(inflater, container, false)
        bindingArchive = FragmentArchiveBinding.inflate(inflater, container, false)
        binding.tvArchiveAnswer.text = textInput

        binding.btnSave.setOnClickListener {
            binding.constraintArchiveSave.visibility = View.GONE
            //여기에 로티와 텍스트 뷰 visibility 설정
            val handler = Handler(Looper.getMainLooper())
            bindingArchive.tvWriteArchive.text = getString(R.string.already_archive)
            bindingArchive.btnWriteArchive.text = getString(R.string.check_archive)
            handler.postDelayed({
                parentFragmentManager.popBackStack("archiveFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }, 1000)

        }

        binding.btnNoSave.setOnClickListener{
            parentFragmentManager.beginTransaction()
                //.remove(this@ArchiveSave)
                .show(ArchiveSave(textInput))
                .commit()
        }
        return binding.root
    }
}