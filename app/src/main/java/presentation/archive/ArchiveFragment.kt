package presentation.archive

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityMainBinding
import com.example.tome_aos.databinding.FragmentArchiveBinding
import com.example.tome_aos.databinding.FragmentArchiveWriteBinding


class ArchiveFragment : Fragment() {
    private lateinit var binding: FragmentArchiveBinding
    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveBinding.inflate(inflater, container, false)
        bindingMain = ActivityMainBinding.inflate(inflater, container, false)

        binding.btnWriteArchive.setOnClickListener {
            val fragment = ArchiveWrite()
            parentFragmentManager.beginTransaction()
                .replace(R.id.archive_framelayout, fragment, "archiveWrite")
                .commit()
        }
        return binding.root
    }
}