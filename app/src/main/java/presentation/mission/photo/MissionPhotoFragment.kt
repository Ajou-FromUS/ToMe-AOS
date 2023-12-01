package presentation.mission.photo

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView.HitTestResult.IMAGE_TYPE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionDetailBinding
import com.example.tome_aos.databinding.FragmentMissionPhotoBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import presentation.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MissionPhotoFragment : Fragment() {
    private lateinit var binding: FragmentMissionPhotoBinding
    private lateinit var showButton: Button
    private lateinit var againButton: Button
    private lateinit var missionImage: ImageView
    private lateinit var detailText: TextView

    private lateinit var currentPhotoPath: String
    private var getPhotoURI: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMissionPhotoBinding.inflate(inflater, container, false).apply {
            showButton = showMissionPhoto
            againButton = chooseAgainBtn
            missionImage = detailImageView
            detailText = missionDetailText
        }

        // 권한 부여 여부
        val isEmpower = ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED


        var chooseOne: Array<String> = arrayOf("카메라로 촬영하기", "앨범에서 선택하기") // 리스트에 들어갈 Array

        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("두 가지 방법 중 고르기!")?.setItems(chooseOne,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == 0){ // 카메라
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    callCamera.launch(intent)
                }else{ // 앨범
                    // 권한 부여 되지 않았을경우
//                    if (isEmpower) {
//                        empowerPhotoandReadStorage()
//                        // 권한 부여 되었을 경우
//                    } else {
//                        val intent = Intent(Intent.ACTION_PICK)
//                        intent.type = "image/*"
//                        callAlbum.launch(intent)
//                    }
                }
            })
        // 다이얼로그를 띄워주기
        builder?.show()

        Log.d("Record isEmpower", isEmpower.toString())


        return binding.root
    }

    private fun openGalleryForImage() {
        Intent(Intent.ACTION_PICK).apply {
            putExtra("crop", true)
            type = MediaStore.Images.Media.CONTENT_TYPE
            choiceAlbumLauncher.launch(this)
        }

    }

    private fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        Constants.FILE_PROVIDER,
                        it
                    )
                    getPhotoURI = photoURI

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat(DATE_TYPE).format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                getPhotoURI?.let { uri ->
                    cropSingleImage(uri)
                }
                return@registerForActivityResult
            }
        }

    private val choiceAlbumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                data.data?.let { dataUri ->
                    Glide.with(binding.detailImageView)
                        .load(it)
                        .transform(CircleCrop())
                        .into(binding.detailImageView)
                }
            }
        }

//    val cursor = requireActivity().contentResolver.query(
//        dataUri,
//        null,
//        null,
//        null,
//        null
//    )
//    cursor?.let { cu ->
//        cu.moveToFirst()
//        val columnIndex = cu.getColumnIndex(MediaStore.Images.Media.DATA)
//        val mediaPath = cu.getString(columnIndex)
//        cu.close()
//
//        val file = File(mediaPath)
//    }

    private val cropPictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val file = File(currentPhotoPath)// 사용하면 됨
                dismiss()
                return@registerForActivityResult
            }
            dismiss()
        }

    private fun cropSingleImage(photoUriPath: Uri) {
        val cropIntent = Intent(CROP_INTENT).apply {
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            setDataAndType(photoUriPath, IMAGE_TYPE)
            putExtra(ASPECT_X, 1)
            putExtra(ASPECT_Y, 1)
            putExtra(SCALE, true)
            putExtra(OUTPUT, photoUriPath)
        }

        val list = requireActivity().packageManager.queryIntentActivities(cropIntent, 0)

        requireActivity().grantUriPermission(
            list[0].activityInfo.packageName,
            photoUriPath,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        requireActivity().grantUriPermission(
            list[0].activityInfo.packageName, photoUriPath,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        val intent = Intent(cropIntent).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            component = ComponentName(list[0].activityInfo.packageName, list[0].activityInfo.name)
        }

        cropPictureLauncher.launch(intent)
    }

    // 레코딩, 파일 읽기 쓰기 권한부여
//    private fun empowerPhotoandReadStorage(){
//        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//        ActivityCompat.requestPermissions(context as MainActivity, permissions,0)
//    }
//
//    private val callAlbum: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
//            val uri = it.data!!.data
//            Glide.with(this)
//                .load(uri)
//                .into(binding.detailImageView)
//        }
//    }
}

