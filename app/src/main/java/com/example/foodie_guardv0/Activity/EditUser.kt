package com.example.foodie_guardv0.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.dataclass.UserChangePassword
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class EditUser : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editUser)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userSharedPreferences = UserSharedPreferences(this)
        val user = userSharedPreferences.getUser()!!.user

        val emailContainer = findViewById<TextView>(R.id.tv_Email)
        val nameContainer = findViewById<TextView>(R.id.tv_Name)
        val surnameContainer = findViewById<TextView>(R.id.tv_Surname)

        val actualPass = findViewById<EditText>(R.id.et_ActualPassword)
        val newPass = findViewById<EditText>(R.id.et_NewPassword)
        val confirmNewPass = findViewById<EditText>(R.id.et_ConfirmNewPassword)

        val buttonSavePass = findViewById<Button>(R.id.bt_SaveNewPassword)
        val buttonDeleteAccount = findViewById<Button>(R.id.bt_DeleteAccount)

        val BackButton = findViewById<ImageButton>(R.id.ib_return)
        BackButton.setOnClickListener() {
            finish()
        }

        emailContainer.text = user.email
        nameContainer.text = user.name
        surnameContainer.text = user.surname

        buttonSavePass.setOnClickListener() {
            if (CheckCurrentPass(user.password, actualPass.text)) {
                if(CheckNewPass(newPass.text, confirmNewPass.text)){
                    val datos = mapOf(
                        "email" to user.email,
                        "password" to user.password,
                        "newPassword" to newPass.text.toString(),
                    )
                    lifecycleScope.launch {
                        changePassword(datos, newPass.text.toString())
                    }
                }
            }
        }

        buttonDeleteAccount.setOnClickListener() {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    deleteAccount(user.id)
                } catch (e: Exception) {

                }
            }
        }

    }

    fun CheckCurrentPass(actualPass: String, inputActualPass: Editable): Boolean {
        if (actualPass == inputActualPass.toString()) {
            return true
        } else {
            Toast.makeText(this, "Contraseña Actual Incorrecta", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun CheckNewPass(newpass: Editable, confirmNewPass: Editable): Boolean {
        if (newpass.toString() == confirmNewPass.toString()){
            return true
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private suspend fun deleteAccount(id : Int){
        return suspendCoroutine { continuation ->
            val call = RetrofitClient.apiService.deleteUser(id)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    userSharedPreferences = UserSharedPreferences(this@EditUser)
                    userSharedPreferences.clearUser()

                    val cerrarSegunda = Intent(this@EditUser, HomeActivity::class.java)
                    cerrarSegunda.putExtra("edituser", true)
                    startActivity(cerrarSegunda)

                    startActivity(Intent(this@EditUser, LoginActivity::class.java))

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun changePassword(body: Map<String,String>, password: String) {
        return suspendCoroutine { continuation ->
            val call = RetrofitClient.apiService.changePassword(body)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    userSharedPreferences = UserSharedPreferences(this@EditUser)
                    val actualUser = userSharedPreferences.getUser()!!.user
                    val user = ActualUser(User(actualUser.id, actualUser.name, actualUser.surname, actualUser.email, password), userSharedPreferences.getUser()!!.token)
                    userSharedPreferences.clearUser()
                    userSharedPreferences.saveUser(user)

                    finish()

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }
            })
        }
    }

}