package com.example.foodie_guardv0.Activity.auth

import android.app.AlertDialog
import android.content.Intent
import com.example.foodie_guard0.R
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guardv0.dataclass.ResponseErrors
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.suspendCoroutine

class RegisterActivity : AppCompatActivity() {

    lateinit var userSharedPreferences: UserSharedPreferences
    lateinit var progressBar: ProgressBar
    lateinit var btnRegister: Button
    lateinit var usernameInput: EditText
    lateinit var lastNameInput: EditText
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var confirmPasswordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)
        userSharedPreferences = UserSharedPreferences(this)

        btnRegister = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.progressBar)
        val back = findViewById<ImageButton>(R.id.backButton)

        back.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {

            usernameInput = findViewById(R.id.name)
            val name = usernameInput.text.toString()

            lastNameInput = findViewById(R.id.lastname)
            val surname = lastNameInput.text.toString()

            emailInput = findViewById(R.id.email)
            val email = emailInput.text.toString()

            passwordInput = findViewById(R.id.passwordInput)
            val password = passwordInput.text.toString()

            confirmPasswordInput = findViewById(R.id.confirm_password)
            val confirmPassword = confirmPasswordInput.text.toString()

            if (password == confirmPassword) {
                val user = User(0, name, surname, email, password, "", 0)
                comprobarDatos(user)
            } else {
                confirmPasswordInput.error = "Las contraseñas no coinciden"
                confirmPasswordInput.requestFocus()
            }
        }
    }

    private fun comprobarDatos(user: User) {
        if (user.name.isEmpty()) {
            usernameInput.error = "Introduce un nombre"
            usernameInput.requestFocus()
        } else if (user.surname.isEmpty()) {
            lastNameInput.error = "Introduce un apellido"
            lastNameInput.requestFocus()
        } else if (user.email.isEmpty()) {
            emailInput.error = "Introduce un correo electrónico"
            emailInput.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            emailInput.error = "Introduce un correo electrónico válido"
            emailInput.requestFocus()
        } else if (user.password.isEmpty()) {
            passwordInput.error = "Introduce una contraseña"
            passwordInput.requestFocus()
        } else if (user.password.length < 8) {
            passwordInput.error = "La contraseña debe tener al menos 8 caracteres"
            passwordInput.requestFocus()
        } else {
            progressBar.visibility = View.VISIBLE
            btnRegister.isClickable = false

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    crearCuenta(user)
                } catch (e: Exception) {
                    Log.e("Resultado", e.message.toString())
                }
            }
        }
    }

    private suspend fun crearCuenta(user: User) {
        return suspendCoroutine {
            val call = apiService.createUser(user)

            call.enqueue(object : Callback<ResponseErrors> {
                override fun onResponse(call: Call<ResponseErrors>, response: Response<ResponseErrors>) {

                    if (response.code() == 201) {
                        Thread.sleep(5000)
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (response.code() == 400) {
                        Thread.sleep(5000)
                        progressBar.visibility = View.GONE
                        btnRegister.isClickable = true

                        alerDialog("Error", "Este correo ya está en uso")

                    } else {
                        Thread.sleep(5000)
                        progressBar.visibility = View.GONE
                        btnRegister.isClickable = true
                        alerDialog("Error", "Ocurrió un error en el servidor")
                    }
                }

                override fun onFailure(call: Call<ResponseErrors>, t: Throwable) {
                    Thread.sleep(5000)
                    progressBar.visibility = View.GONE
                    btnRegister.isClickable = true

                    alerDialog("Error", "No se puede establecer conexión con el servidor")
                }
            })
        }
    }

    fun alerDialog(tittle: String, content: String) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setTitle(tittle)
            setMessage(content)
        }.create().show()
    }
}
