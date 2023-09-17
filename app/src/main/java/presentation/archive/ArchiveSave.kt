package presentation.archive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentArchiveSaveBinding

class ArchiveSave(private val textInput: String) : Fragment() {
    private lateinit var binding: FragmentArchiveSaveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveSaveBinding.inflate(inflater, container, false)
        binding.tvArchiveAnswer.text = textInput
        return binding.root
    }
}