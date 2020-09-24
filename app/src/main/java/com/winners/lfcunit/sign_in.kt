package com.winners.lfcunit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth


private lateinit var sign_in_button:Button
private lateinit var email_text_view:EditText
private lateinit var password_text_view:EditText

class signIn : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
//        val user = FirebaseAuth.getInstance().currentUser

        sign_in_button = findViewById(R.id.sign_in_user)
        email_text_view = findViewById(R.id.sign_in_email)
        password_text_view = findViewById(R.id.sign_in_password)
        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener(this)

    }

    fun signInUserWithFirebase(email:String,pass:String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){
                task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                toastMessage(this, ""+user)
                UpdateUI(user, this)
            }else{
                UpdateUI(context =  this)
            }
        }
    }

    override fun onClick(v: View?) {
        val usermail = email_text_view.text.toString()
        val password = password_text_view.text.toString()

        Log.d("TAG SIgnIN", ""+usermail+"and password is "+password)

        if (v!!.id == R.id.sign_in_user){
            if ((usermail != "") && (password != ""))
                signInUserWithFirebase(usermail, password)
            else {
                if (usermail == "") toastMessage(this, "Please Enter Email")
                if (password == "") toastMessage(this, "Please enter password")
            }
        }
        toastMessage(this, "in Sign-In Activity")
    }

}
