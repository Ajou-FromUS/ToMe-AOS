package presentation.archive

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            val prevFrag = parentFragmentManager.findFragmentByTag("archiveWrite")
            val handler = Handler(Looper.getMainLooper())
            val handlerWrite = Handler(Looper.getMainLooper())

            binding.constraintArchiveSave.visibility = View.GONE
            //여기에 로티와 텍스트 뷰 visibility 설정

            bindingArchive.tvWriteArchive.text = getString(R.string.already_archive)
            bindingArchive.btnWriteArchive.text = getString(R.string.check_archive)
            prevFrag?.let{
                handler.postDelayed({
                    parentFragmentManager.beginTransaction()
                        .remove(this@ArchiveSave)
                        .show(it)
                        .replace(R.id.frame_archive, ArchiveFragment())
                        .commit()
                }, 1000)
            }
        }

        binding.btnNoSave.setOnClickListener{
            val prevFrag = parentFragmentManager.findFragmentByTag("archiveWrite")
            prevFrag?.let {
                parentFragmentManager.beginTransaction()
                    //.show(it) // 이전 프래그먼트를 다시 표시
                    .remove(this@ArchiveSave) // ArchiveSave 프래그먼트를 제거
                    .commit()
            }
        }
        return binding.root
    }
}