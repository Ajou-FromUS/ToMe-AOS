package presentation.home

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tome_aos.databinding.FragmentHomeBinding
import presentation.dialog.SnackDialog


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isFabOpen = false // Fab 버튼 default는 닫혀있음

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFABClickEvent()
        setSnackClickEvent()
    }

    private fun setSnackClickEvent() {
        binding.snackBtn.setOnClickListener {
            val dialog = SnackDialog()
            dialog.show(activity?.supportFragmentManager!!, "SnackDialog")
        }
    }

    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabMain.setOnClickListener {
            if (!isFabOpen) {
                openFab()
            } else {
                closeFab()
            }
            isFabOpen = !isFabOpen
        }

        if (binding.fab2.translationY == 0f) {
            binding.fabBg.visibility = View.GONE
            binding.fab1.visibility = View.GONE
            binding.fab2.visibility = View.GONE
        }

        // 플로팅 버튼 클릭 이벤트 - 도감
        binding.fab1.setOnClickListener {
            Toast.makeText(this.context, "도감 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }

        // 플로팅 버튼 클릭 이벤트 - 상점
        binding.fab2.setOnClickListener {
            Toast.makeText(this.context, "상점 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFab() {
        val animation2 = AlphaAnimation(0f, 1f)
        animation2.duration = 100
        animation2.fillAfter = true
        binding.fab1.startAnimation(animation2)
        binding.fab2.startAnimation(animation2)
        binding.fabBg.startAnimation(animation2)

        binding.fabMain.isSelected = true
        ObjectAnimator.ofFloat(binding.fab1, "translationY", 40f).apply { start() }
        ObjectAnimator.ofFloat(binding.fab2, "translationY", 90f).apply { start() }
        binding.fabBg.animateViewHeight(100, 0, 154)

        binding.fabBg.visibility = View.VISIBLE
        binding.fab1.visibility = View.VISIBLE
        binding.fab2.visibility = View.VISIBLE
    }

    private fun closeFab() {
        val animation1 = AlphaAnimation(1f, 0f)
        animation1.duration = 300
        animation1.fillAfter = true
        binding.fab1.startAnimation(animation1)
        binding.fab2.startAnimation(animation1)
        binding.fabBg.startAnimation(animation1)

        binding.fabMain.isSelected = false
        ObjectAnimator.ofFloat(binding.fab1, "translationY", 0f).apply { start() }
        ObjectAnimator.ofFloat(binding.fab2, "translationY", 0f).apply { start() }

        binding.fabBg.animateViewHeight(300, 154, 1)
    }
    fun View.animateViewHeight(duration: Long, start: Int, end: Int) {
        val valueAnimator = ValueAnimator.ofInt(start, end)
        valueAnimator.addUpdateListener { animation ->
            val new = animation.animatedValue as Int
            val params = this@animateViewHeight.layoutParams as ViewGroup.LayoutParams
            params.height = new
            this@animateViewHeight.layoutParams = params
        }
        valueAnimator.duration = duration
        valueAnimator.start()
    }
}

