package presentation.mission.photo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.canhub.cropper.CropImageContract
import com.example.tome_aos.R
import com.example.tome_aos.databinding.FragmentMissionPhotoBinding
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.MissionImageService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.http.hasBody
import okio.BufferedSink
import okio.source
import presentation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MissionPhotoFragment : Fragment() {
    private lateinit var binding: FragmentMissionPhotoBinding
    private lateinit var showButton: Button
    private lateinit var againButton: Button
    private lateinit var missionImage: ImageView
    private lateinit var detailText: TextView
//    private lateinit var baseLayout:

    lateinit var bitmap: Bitmap

    private lateinit var currentPhotoPath: Uri

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

        val missionTitle = arguments?.getString("missionTitle")
        val missionID = arguments?.getInt("missionID")

        detailText.text = missionTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }

        // 권한 부여 여부
        val isEmpower = ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        // 현재 기기에 설정된 쓰기 권한을 가져오기 위한 변수
        var writePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )
        // 현재 기기에 설정된 읽기 권한을 가져오기 위한 변수
        var readPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)

        chooseImage()

        showButton.setOnClickListener {
            Log.d("Image uri", currentPhotoPath.toString())
            if(currentPhotoPath != null){
                patchImageMission(currentPhotoPath, missionID)
            }
        }

        againButton.setOnClickListener {
            chooseImage()
        }

        return binding.root
    }

    private fun chooseImage(){
        var chooseOne: Array<String> = arrayOf("카메라로 촬영하기", "앨범에서 선택하기") // 리스트에 들어갈 Array
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("두 가지 방법 중 고르기!")?.setItems(chooseOne,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == 0){ // 카메라
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    callCamera.launch(intent)
                }else{ // 앨범
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    intent.putExtra("crop", true)
                    callAlbum.launch(intent)
                }
            })
        builder?.show()
    }

    @SuppressLint("ResourceAsColor")
    private fun imageError(){
        showButton.visibility = View.INVISIBLE
        againButton.text = "사진 다시 선택하기"
        againButton.background.setTint(R.color.color_main)
    }


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
        } else {
            // An error occurred.
            val exception = result.error
        }
    }

//    private fun startCrop() {
//        // Start picker to get image for cropping and then use the image in cropping activity.
//        cropImage.launch(
//            options {
//                setGuidelines(CropImageView.Guidelines.ON)
//            }
//        )
//
//        // Start picker to get image for cropping from only gallery and then use the image in cropping activity.
//        cropImage.launch(
//            options {
//                setImagePickerContractOptions(
//                    PickImageContractOptions(includeGallery = true, includeCamera = false)
//                )
//            }
//        )
//
//        // Start cropping activity for pre-acquired image saved on the device and customize settings.
//        cropImage.launch(
//            options(uri = imageUri) {
//                setGuidelines(Guidelines.ON)
//                setOutputCompressFormat(CompressFormat.PNG)
//            }
//        )
//    }
//
//    fun onSelectImageClick(view: View?) {
//        activity(null).setGuidelines(Guidelines.ON).start(this)
//    }

//    private fun cropImage(uri: Uri?){
//        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
//            .setCropShape(CropImageView.CropShape.RECTANGLE)
//            //사각형 모양으로 자른다
//            .start(this)
//    }


//    private fun startCapture() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    null
//                }
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        requireActivity(),
//                        "com.example.tome_aos.file-provider",
//                        it
//                    )
//                    getPhotoURI = photoURI
//
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    takePictureLauncher.launch(takePictureIntent)
//                }
//            }
//        }
//    }

//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        Log.d("image", "createImageFile")
//        val timeStamp: String = SimpleDateFormat().format(Date())
//        val storageDir: File? =
//            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpeg",
//            storageDir
//        ).apply {
//            currentPhotoPath = absolutePath
//        }
//    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                currentPhotoPath?.let { uri ->
                    //cropSingleImage(uri)
                }
                return@registerForActivityResult
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

//    private val cropPictureLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                val file = File(currentPhotoPath)// 사용하면 됨
//                return@registerForActivityResult
//            }
//        }

//    private fun cropSingleImage(photoUriPath: Uri) {
//        val cropIntent = Intent(CROP_INTENT).apply {
//            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            setDataAndType(photoUriPath, IMAGE_TYPE)
//            putExtra(ASPECT_X, 1)
//            putExtra(ASPECT_Y, 1)
//            putExtra(SCALE, true)
//            putExtra(OUTPUT, photoUriPath)
//        }
//
//        val cIntent = Intent(Intent.Cr)
//
//        val list = requireActivity().packageManager.queryIntentActivities(cropIntent, 0)
//
//        requireActivity().grantUriPermission(
//            list[0].activityInfo.packageName,
//            photoUriPath,
//            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
//
//        requireActivity().grantUriPermission(
//            list[0].activityInfo.packageName, photoUriPath,
//            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        )
//
//        val intent = Intent(cropIntent).apply {
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            component = ComponentName(list[0].activityInfo.packageName, list[0].activityInfo.name)
//        }
//
//        cropPictureLauncher.launch(intent)
//    }


    private fun getRealPathFromURI(uri: Uri, context: Context): String {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            // Android 10 이상에서는 contentResolver.openInputStream 사용
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "tome_image.jpeg")
            file.createNewFile()
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }

    @SuppressLint("Range")
    fun Uri.asMultipart(name: String, contentResolver: ContentResolver): MultipartBody.Part? {
        return contentResolver.query(this, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return contentResolver.getType(this@asMultipart)?.toMediaType()
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                    }
                }
                it.close()
                MultipartBody.Part.createFormData("mission_image","image.jpeg", requestBody)
            } else {
                it.close()
                null
            }
        }
    }

    private val callAlbum: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
            val uri = it.data!!.data
            currentPhotoPath = uri!!

            Glide.with(this)
                .load(uri)
                .into(binding.detailImageView)
        }
    }

    private val callCamera: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
            val extras = it.data?.extras
            bitmap = extras?.get("data") as Bitmap
            binding.detailImageView.setImageBitmap(bitmap)
        }
    }

    private fun patchImageMission(uri: Uri, missionID: Int?) {
        val file = File(getRealPathFromURI(uri, requireContext()))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        //val missionImage: MultipartBody.Part = MultipartBody.Part.createFormData("mission_image","image.jpeg", requestFile)
        val missionImage: MultipartBody.Part = Uri.asMultipart()

        Log.d("Image patch", missionImage.toString())

        val client = ApiClient.getApiClient().create(MissionImageService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            client.patchImageMissions(accessToken, refreshToken, missionID, missionImage).enqueue(object :
                Callback<MissionResponse.Data> {
                override fun onResponse(call: Call<MissionResponse.Data>, response: Response<MissionResponse.Data>) {
                    if (response.isSuccessful) {
                        Log.d("Image success", response.body().toString())
                    } else {
                        Log.d("Image else", "code: " + response.code().toString() + ", body: " + response.message())
                        imageError()
                    }
                }
                override fun onFailure(call: Call<MissionResponse.Data>, t: Throwable) {
                    Log.d("Image fail", t.toString())
                }
            })
        }
    }
}

