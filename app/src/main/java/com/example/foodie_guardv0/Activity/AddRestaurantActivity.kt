package com.example.foodie_guardv0.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import javax.security.auth.Subject

class AddRestaurantActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_restaurant)

        val sendButton = findViewById<Button>(R.id.button3)
        val back = findViewById<ImageButton>(R.id.backButton)

        val name = findViewById<EditText>(R.id.addRestaurantName)
        val address = findViewById<EditText>(R.id.addRestaurantAddress)
        val phone = findViewById<EditText>(R.id.addRestaurantPhone)
        val type = findViewById<EditText>(R.id.addRestaurantType)
        val email = findViewById<EditText>(R.id.addRestaruantEmail)


        sendButton.setOnClickListener{

            val recipient = "Solicitud para añadir un restaurante"
            val subject = "Solicitud para añadir un restaurante"
            val message = "Nombre del restaurante: ${name.text}\nDirección: ${address.text}\nTeléfono: ${phone.text}\nTipo de comida: ${type.text}\nCorreo electrónico: ${email.text}"

            sendEmail(recipient, subject, message)
        }

        back.setOnClickListener() {
            finish()
        }

    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto: samirobayo04@gmail.com")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

}