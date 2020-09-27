package com.winners.lfcunit

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.winners.lfcunit.validators.validator


private lateinit var sign_in_button:Button
private lateinit var email_text_view:EditText
private lateinit var password_text_view:EditText
private lateinit var dialog:ProgressDialog

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
        dialog = ProgressDialog(this)

    }



    fun signInUserWithFirebase(email:String,pass:String) {
        Log.d("TAG_SIGN_IN", ""+email+" "+pass)
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){
                task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                if (user != null) {
                    Log.d("TAG_SIGN_IN", ""+user.email)
                    toastMessage(this, ""+user)
                    dialog.dismiss()
                }

//                UpdateUI(user, this)
            }else{
                Log.d("TAG_SIGN_IN", "User not successfully Signed In")
                UpdateUI(context =  this)
            }
        }
    }

    fun signInUser(){
        val email = email_text_view.text.toString()
        val password = password_text_view.text.toString()
        if (email.isNotBlank() && password.isNotBlank()){
            if (validator.regex(validator.emailPattern, email)) {
                if (password.length >= 6) {
                    dialog.setTitle("Signing In")
                    dialog.setMessage("Confirming Users credentials")
                    dialog.show()
                    signInUserWithFirebase(email, password)
                }
                else{
                    setErrorRequired(password_text_view,
                        "Password is not up to 6 characters")
                }
            }else{
                setErrorRequired(email_text_view,
                    "Email is in valid")
            }
        }else{
            setErrorRequired(password_text_view,
                "Password is not up to 6 characters")
            setErrorRequired(email_text_view,
                "Email is in valid")
        }
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.sign_in_user){
            signInUser()
        }else toastMessage(this, "in Sign-In Activity")
    }

}
