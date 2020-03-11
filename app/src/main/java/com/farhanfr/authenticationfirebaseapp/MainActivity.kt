package com.farhanfr.authenticationfirebaseapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers : List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE:Int = 100;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        showSignInOptions()

        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled=false
                    showSignInOptions()
                }
                .addOnFailureListener {
                    e -> Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+ user!!.email,Toast.LENGTH_LONG).show()

                btn_sign_out.isEnabled = true
            }
            else{
                Toast.makeText(this,""+ response!!.error!!.message,Toast.LENGTH_LONG).show()
            }
        }
    }
}
