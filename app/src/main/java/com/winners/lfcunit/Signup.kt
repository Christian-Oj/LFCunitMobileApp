package com.winners.lfcunit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


private lateinit var sign_up_button:Button
private lateinit var gmail_sign_Button:Button
private lateinit var user_name:TextView
private lateinit var email:TextView
private lateinit var password:TextView
private lateinit var sign_in:TextView
lateinit var signCLient:GoogleSignInClient
lateinit var gso: GoogleSignInOptions
lateinit var account:GoogleSignInAccount
public var GOOGLE_SIGN_IN_Activity:Int = 200
private val realtimeDatabaseManager by lazy { Realtime_data()}
private const val TAG:String = "SIGN_UP_ACTIVITY"

class Signup : AppCompatActivity(), View.OnClickListener {
    private lateinit var googleSign:Intent
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sign_up_button = findViewById(R.id.sign_up_button)
        gmail_sign_Button = findViewById(R.id.sign_up_with_mail)
        user_name = findViewById(R.id.username_view)
        email = findViewById(R.id.email_view)
        password = findViewById(R.id.password_view)
        sign_in = findViewById(R.id.sign_in)

        auth = FirebaseAuth.getInstance()

        setListener()
        configRequest()
        signCLient = GoogleSignIn.getClient(this, gso)
        toastMessage(this, "am on create function")

    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(USER_EMAIL, google_user.email)
//        outState.putString(USER_DISPLAYNAME, google_user.displayName)
//        outState.putString(USER_PASSWORD, "GUest")
//        Log.d(TAG, "onSaveInstanceState: User Details: ${google_user.email}")
//    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        UpdateUI(context=this, user=currentUser)
        toastMessage(this, ""+currentUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    private fun configRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private fun setListener(){
        sign_up_button.setOnClickListener(this)
        gmail_sign_Button.setOnClickListener(this)
        sign_in.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.sign_up_button -> createUserwithEmailPassword()
                R.id.sign_up_with_mail -> GoogleSign()
                R.id.sign_in -> signInActivity()
                else -> toastMessage(this, "No")
            }
        }else toastMessage(this, "crashed")

    }

    private fun signInActivity() {
        val intent = Intent(this, signIn::class.java)
        startActivity(intent)
    }

    private fun selectUnitActivity(user: FirebaseUser?){
        val intent = Intent(this, user_branch_and_unit::class.java)
        intent.putExtra(USER_EMAIL,user!!.email)
        intent.putExtra(USER_DISPLAYNAME, user.displayName)
        intent.putExtra(USER_PASSWORD, "GUEST USER")

        startActivity(intent)

    }

    private fun GoogleSign() {
        googleSign = signCLient.signInIntent
        startActivityForResult(googleSign, GOOGLE_SIGN_IN_Activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_Activity){
            var task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthwithGoogle(account.idToken!!)
            }catch (e:ApiException){
                toastMessage(this, ""+e.message)
                Log.d("TAG", ""+e.message.toString())
            }

        }
    }

//    private fun handleSignedResult(task: Task<GoogleSignInAccount>) {
//        try {
//            val account = task.getResult(ApiException::class.java)
//            updateUI(account)
//        }catch (e:ApiException){
//            Throwable(e)
//            toastMessage(this, e.message.toString())
//            Log.d("TAG", "Signed Result Failed"+e.message)
//            updateUI(null)
//        }
//    }


    private fun firebaseAuthwithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this){task ->
            if (task.isSuccessful){
                val user = auth.currentUser
                toastMessage(this, "Google Firebase is successfully signed in")
                UpdateUI(context=this, user=user)
                if (user != null) {
                    Log.d(TAG,""+user.email+" "+user.displayName+" "+user.photoUrl)
                    toastMessage(this, ""+user.email+" "+user.displayName+" "+user.photoUrl)
                    selectUnitActivity(user)
//                    google_user = user
//                    SaveUserToDB(user)
                }
            }else{
                toastMessage(this, "Google Firebase is not successfully signed in")
                UpdateUI(context = this)
            }
        }
    }

    private fun createUserwithEmailPassword(){
        val usermail = email.text.toString()
        val password = password.text.toString()
        if (usermail != null && password != null){
            auth.createUserWithEmailAndPassword(usermail, password)
                .addOnCompleteListener(this){task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
//                        toastMessage(this, "Firebase is successfully signed in")
                        UpdateUI(context=this, user=user)
                        if (user != null) {
                            Log.d(TAG,""+user.email+" "+user.displayName+" "+user.photoUrl)
                            toastMessage(this, ""+user.email+" "+user.displayName+" "+user.photoUrl)
//                            SaveUserToDB(user)
                        }
                    }else{
                        toastMessage(this, "Firebase is not successfully signed in")
                        UpdateUI(context = this)
                    }
                }
        }else toastMessage(this, "email and password not supplied")

    }

    fun SaveUserToDB(email:String,password:String,display_name:String,branch_name:String,unit_name:String){
        val userObj = User(email,password, display_name)
        realtimeDatabaseManager.saveUser(userObj,branch_name,unit_name,onSuccessAction,onFailureAction)
        toastMessage(this, "Save User to DB ${this.localClassName}")
    }

    val onSuccessAction:() -> Unit = { println("User Created Successfully") }
    val onFailureAction:() -> Unit = { println("User not Created") }


    fun signOut() = FirebaseAuth.getInstance().signOut()

    companion object{
        val USER_EMAIL = "USER_EMAIL"
        val USER_DISPLAYNAME = "USER_DISPLAYNAME"
        val USER_PASSWORD = "USER_PASSWORD"
        private lateinit var google_user:FirebaseUser

    }

//    private fun getUsers() = Realtime_data().getUser
}