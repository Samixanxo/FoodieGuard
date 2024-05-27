package com.example.foodie_guardv0.Activity.actions.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.HomeActivity
import com.example.foodie_guardv0.Activity.auth.LoginActivity
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
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
                    val dataChangePass = mapOf(
                        "email" to user.email,
                        "password" to user.password,
                        "newPassword" to newPass.text.toString(),
                    )

                    val userData = mapOf(
                        "token" to userSharedPreferences.getUser()!!.token,
                        "user" to user.id.toString()
                    )
                    lifecycleScope.launch {
                        if(identificationConfirm(userData)){
                            changePassword(dataChangePass, newPass.text.toString())
                        } else{
                            showPopUpMenu()
                        }

                    }
                }
            }
        }

        buttonDeleteAccount.setOnClickListener() {
            confirmationDialog("¿Seguro que quieres borrar tu cuenta?")
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
        val newPassStr = newpass.toString()
        val confirmNewPassStr = confirmNewPass.toString()

        if (newPassStr.length < 8) {
            Toast.makeText(this, "La nueva contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPassStr == confirmNewPassStr) {
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

                    val datos = mapOf(
                        "email" to userSharedPreferences.getUser()!!.user.email,
                        "password" to password
                    )
                    val call = RetrofitClient.apiService.postUser(datos)
                    call.enqueue(object : Callback<ActualUser>{
                        override fun onResponse(call: Call<ActualUser>,response: Response<ActualUser>) {
                            userSharedPreferences.clearUser()
                            userSharedPreferences.saveUser(response.body()!!)
                            Toast.makeText(this@EditUser, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        override fun onFailure(call: Call<ActualUser>, t: Throwable) {
                        }
                    })

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }
            })
        }
    }

    suspend fun identificationConfirm(body: Map<String,String>) : Boolean {
        return suspendCoroutine { continuation ->
            val call = RetrofitClient.apiService.confirmUser(body)
            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    val confirmation = response.body()!!
                    continuation.resume(confirmation)
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {

                }
            })
        }
    }

    private fun confirmationDialog(message:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de conexión")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val user = userSharedPreferences.getUser()!!.user
                    deleteAccount(user.id)
                } catch (e: Exception) {

                }
            }
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()
    }


    private fun showPopUpMenu() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No es posible modificar la contraseña debido a que no coinciden las credenciales. Si este error persiste, cierre sesion y vuelva a intentarlo")
        builder.setPositiveButton(android.R.string.ok){dialog, which ->
            Toast.makeText(this, "Error en el cambio de contraseña", Toast.LENGTH_SHORT).show()}
        builder.show()
    }

}
