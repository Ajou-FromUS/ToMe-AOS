package presentation.archive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentArchiveBinding


class ArchiveFragment : Fragment() {
   private lateinit var binding: FragmentArchiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveBinding.inflate(inflater, container, false)

        binding.btnWriteArchive.setOnClickListener {
            val fragment = ArchiveWrite()

            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.frame_archive, fragment)
            transaction.commit()
        }
        return binding.root
    }
}