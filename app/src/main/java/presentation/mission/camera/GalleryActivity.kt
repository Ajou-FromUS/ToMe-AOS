package presentation.mission.camera

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tome_aos.R
import com.example.tome_aos.databinding.ActivityGalleryBinding
import com.example.tome_aos.databinding.ActivityLandingBinding
import com.example.tome_aos.databinding.ActivityMainBinding
import com.example.tome_aos.databinding.FragmentMissionDetailBinding
import java.io.File

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button4.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
        //imageView = findViewById(R.id.imageView5)
    }

    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null){
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.imageView5)
        }
    }
    companion object {
        const val REVIEW_MIN_LENGTH = 10

        //갤러리 권한 요청
        const val REQ_GALLERY = 1

        //API 호출 시 Parameter key값
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }
    //이미지 결과값으로 받는 함수
    private val imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //이미지 받으면 imageview에 적용
                val imageUri = result.data?.data
                imageUri?.let {
                    //서버 업로드를 위한 파일 형태로 변환
                    val imageFile = File(getRealPathFromURI(it))
                    //이미지 불러옴
                    Glide.with(this)
                        .load(imageUri)
                        .fitCenter()
                        .apply(RequestOptions().override(500, 500))
                        .into(imageView)
                }
            }
        }
    fun getRealPathFromURI(uri: Uri): String{
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()

        return result
    }

    private fun selectGallery(){
        val writePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)

        if(writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE), REQ_GALLERY)
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            imageResult.launch(intent)
        }
    }
}