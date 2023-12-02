package presentation.mission.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.canhub.cropper.CropImage.CancelledResult.bitmap
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageView.Guidelines
import com.example.tome_aos.databinding.FragmentMissionPhotoBinding
import com.google.gson.GsonBuilder
import data.dto.request.MissionCompleteRequest
import data.dto.request.MissionImageRequest
import data.dto.response.MissionResponse
import data.service.ApiClient
import data.service.MissionCompleteService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import presentation.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
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

    lateinit var bitmap: Bitmap

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

        val missionTitle = arguments?.getString("missionTitle")
        val missionID = arguments?.getInt("missionID")

        detailText.text = missionTitle

        // 권한 부여 여부
        val isEmpower = ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context as MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED


        var chooseOne: Array<String> = arrayOf("카메라로 촬영하기", "앨범에서 선택하기") // 리스트에 들어갈 Array

        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("두 가지 방법 중 고르기!")?.setItems(chooseOne,
            DialogInterface.OnClickListener { dialog, which ->
                if (which == 0){ // 카메라
                    startCapture()
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    callCamera.launch(intent)
                }else{ // 앨범
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    intent.putExtra("crop", true)
                    callAlbum.launch(intent)
                    //openGalleryForImage()
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

        return binding.root
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
                        "com.example.tome_aos.file-provider",
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
        Log.d("image", "createImageFile")
        val timeStamp: String = SimpleDateFormat().format(Date())
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
                    //cropSingleImage(uri)
                }
                return@registerForActivityResult
            }
        }

    private val choiceAlbumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                data.data?.let { dataUri ->
                    Glide.with(this)
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
                return@registerForActivityResult
            }
        }

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

    // 레코딩, 파일 읽기 쓰기 권한부여
//    private fun empowerPhotoandReadStorage(){
//        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//        ActivityCompat.requestPermissions(context as MainActivity, permissions,0)
//    }
//
    private val callAlbum: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
            val uri = it.data!!.data

            val file = File(absolutelyPath(uri, requireContext()))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("proFile", file.name, requestFile)

            Glide.with(this)
                .load(uri)
                .into(binding.detailImageView)
        }
    }

    private val callCamera: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK && it.data != null){
            val extras = it.data!!.extras
            bitmap = extras?.get("data") as Bitmap
            binding.detailImageView.setImageBitmap(bitmap)
        }
    }

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private fun patchImageMission(content: MultipartBody.Part?, missionID: Int?) {
        val client = ApiClient.getApiClient().create(MissionCompleteService::class.java)
        lifecycleScope.launch {
            val accessToken = ApplicationClass.getInstance().getDataStore().accessToken.first()
            val refreshToken = ApplicationClass.getInstance().getDataStore().refreshToken.first()
            val missionImageRequest = MissionImageRequest(content)
            val requestBody = GsonBuilder()
                .serializeNulls().create()
                .toJson(missionImageRequest)
                .toRequestBody("application/json".toMediaTypeOrNull())
            client.getMissions(accessToken, refreshToken, missionID, requestBody).enqueue(object :
                Callback<MissionResponse.Data> {
                override fun onResponse(call: Call<MissionResponse.Data>, response: Response<MissionResponse.Data>) {
                    if (response.isSuccessful) {
                    } else {
                        println("HTTP 오류: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<MissionResponse.Data>, t: Throwable) {
                    Log.d("fail", t.toString())
                    t.printStackTrace()
                }
            })
        }
    }
}

