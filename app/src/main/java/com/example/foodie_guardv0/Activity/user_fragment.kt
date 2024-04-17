package com.example.foodie_guardv0.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class user_fragment : Fragment() {
    lateinit var userSharedPreferences: UserSharedPreferences
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private val SELECT_PHOTO = 2
    lateinit var progressBar : ProgressBar
    lateinit var changeImageButton : Button


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user, container, false)

        if (container != null) {
            userSharedPreferences = UserSharedPreferences(container.context)
        }

        val actualUser = userSharedPreferences.getUser()!!.user
        view.findViewById<TextView>(R.id.tv_User).text =
            actualUser!!.name + " " + actualUser!!.surname
        view.findViewById<TextView>(R.id.tv_Email).text = actualUser!!.email

        progressBar = view.findViewById(R.id.progressBar)


        val buttonEditUser = view.findViewById<Button>(R.id.bt_editUser)
        buttonEditUser.setOnClickListener() {
            startActivity(Intent(activity, EditUser::class.java))
        }
        changeImageButton = view.findViewById(R.id.bt_changeImage)
        changeImageButton.setOnClickListener {
            checkAndRequestPermissions()
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, SELECT_PHOTO)
        }

        val imageView = view.findViewById<ImageView>(R.id.imagetochange)

        if (actualUser.image != null && actualUser.image.isNotEmpty()) {
            val decodedString = Base64.decode(actualUser.image, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Glide.with(this)
                .load(decodedByte)
                .centerCrop()
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.em)
        }
        Log.e("subida", actualUser.image)

        val logoutButton = view.findViewById<Button>(R.id.bt_CloseSession)
        logoutButton.setOnClickListener {
            userSharedPreferences.clearUser()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val ownerButton = view.findViewById<Button>(R.id.bt_NewRestaurant)
        ownerButton.setOnClickListener {
            val intent = Intent(activity, AddRestaurantActivity::class.java)
            startActivity(intent)
        }

        val premiumButton = view.findViewById<Button>(R.id.bt_premium)
        premiumButton.setOnClickListener {
            val intent = Intent(activity, PremiumActivity::class.java)
            startActivity(intent)
        }
        val aboutUsButton = view.findViewById<Button>(R.id.bt_AboutUs)
        aboutUsButton.setOnClickListener {
            val intent = Intent(activity, AboutUsActivity::class.java)
            startActivity(intent)
        }
        val contactButton = view.findViewById<Button>(R.id.bt_Contact)
        contactButton.setOnClickListener {

        }

        return view
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permiso concedido
                } else {
                    // Permiso denegado
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val imageView = view?.findViewById<ImageView>(R.id.imagetochange)
            imageView?.setImageURI(imageUri)
            imageUri?.let { uri ->
                val imagePath = getFileFromUri(requireContext(), uri)
                imagePath?.let { file ->
                    compressAndUploadImage(file)
                }
            }
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image") // Crea un archivo temporal
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun compressAndUploadImage(originalFile: File) {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(originalFile.path, options)

        options.inSampleSize = calculateInSampleSize(options, 1024, 1024)
        options.inJustDecodeBounds = false

        val compressedBitmap = BitmapFactory.decodeFile(originalFile.path, options)
        val compressedFile = File(requireContext().cacheDir, "compressed_image.jpg")
        FileOutputStream(compressedFile).use { out ->
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out) // Compress quality can be changed
        }

        uploadImageToServer(compressedFile)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun uploadImageToServer(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val actualUser = userSharedPreferences.getUser()!!.user
        val userId = actualUser.id
        val call = RetrofitClient.apiService.updateImage(userId, body)
        progressBar.visibility = View.VISIBLE
        changeImageButton.isClickable = false
        Thread.sleep(3000)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Thread.sleep(2000)
                    progressBar.visibility = View.GONE
                    changeImageButton.isClickable = true
                    Log.e("subida", "Image uploaded successfully")
                    updateUserSharedPreferences(actualUser)
                } else {
                    Log.e("subida", "Failed to upload image")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("subida", "Error en la solicitud POST: ${t.message}")
            }
        })
    }

    private fun updateUserSharedPreferences(user: User) {
        val datos = mapOf("email" to user.email, "password" to user.password)
        val call = RetrofitClient.apiService.postUser(datos)
        call.enqueue(object : Callback<ActualUser> {
            override fun onResponse(call: Call<ActualUser>, response: Response<ActualUser>) {
                if (response.isSuccessful) {
                    userSharedPreferences.clearUser()
                    userSharedPreferences.saveUser(response.body()!!)
                    Log.e("subida", "User updated in SharedPreferences")
                } else {
                    Log.e("subida", "Failed to update user data")
                }
            }

            override fun onFailure(call: Call<ActualUser>, t: Throwable) {
                Log.e("subida", "Error updating user in SharedPreferences: ${t.message}")
            }
        })
    }

}


