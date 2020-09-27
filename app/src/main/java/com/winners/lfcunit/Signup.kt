package com.winners.lfcunit

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
import com.winners.lfcunit.validators.validator
import kotlinx.android.synthetic.main.activity_signup.*


private lateinit var sign_up_buttonView:Button
private lateinit var gmail_sign_Button:Button
private lateinit var user_name:EditText
private lateinit var email:EditText
private lateinit var password:EditText
private lateinit var confirm_password_:EditText
private lateinit var sign_inVIew:TextView
lateinit var signCLient:GoogleSignInClient
lateinit var gso: GoogleSignInOptions
lateinit var account:GoogleSignInAccount
private lateinit var select_branch_name_spinner:Spinner
private lateinit var select_unit_name_sign_spinner:Spinner
public var GOOGLE_SIGN_IN_Activity:Int = 200
private val realtimeDatabaseManager by lazy { Realtime_data()}
private lateinit var Dialog:ProgressDialog
private const val TAG:String = "SIGN_UP_ACTIVITY"

class Signup : AppCompatActivity(), View.OnClickListener {
    private lateinit var googleSign:Intent
    private lateinit var auth:FirebaseAuth
    private lateinit var branch_name: String
    private lateinit var unit_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sign_up_buttonView = findViewById(R.id.sign_up_button)
        gmail_sign_Button = findViewById(R.id.sign_up_with_mail)
        user_name = findViewById(R.id.username_view)
        email = findViewById(R.id.email_view)
        password = findViewById(R.id.password_view)
        sign_inVIew = findViewById(R.id.sign_in)
        select_branch_name_spinner = findViewById(R.id.branch_name_signUp)
        select_unit_name_sign_spinner = findViewById(R.id.select_unit_name_signUp)
        confirm_password_ = findViewById(R.id.confirm_password)

        auth = FirebaseAuth.getInstance()
        Dialog = ProgressDialog(this)

        setListener()
        configRequest()
        signCLient = GoogleSignIn.getClient(this, gso)

        SpinnerAdapterSignUp()

        realtimeDatabaseManager.changeListener

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
        Realtime_data.user_id = auth.uid.toString()
        UpdateUI(context = this, user = currentUser)
        toastMessage(this, "" + currentUser)
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
        sign_up_buttonView.setOnClickListener(this)
        gmail_sign_Button.setOnClickListener(this)
        sign_inVIew.setOnClickListener(this)
    }

    private fun SpinnerAdapterSignUp() {
        select_unit_name_sign_spinner.adapter = assignSpinnerAdapter(this, R.array.units)
        select_branch_name_spinner.adapter = assignSpinnerAdapter(this, R.array.church_branch)
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
        intent.putExtra(USER_EMAIL, user!!.email)
        intent.putExtra(USER_DISPLAYNAME, user.displayName)
        intent.putExtra(USER_PASSWORD, "GUEST USER")
        intent.putExtra(USER_ID, user.uid)

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
            }catch (e: ApiException){
                toastMessage(this, "" + e.message)
                Log.d("TAG", "" + e.message.toString())
            }

        }
    }

    private fun firebaseAuthwithGoogle(idToken: String){
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val firebaseuser = auth.currentUser
                    toastMessage(this, "Google Firebase is successfully signed in")
                    UpdateUI(context = this, user = firebaseuser)
                    if (firebaseuser != null) {
                        Log.d(TAG, "" + firebaseuser.email + " " + firebaseuser.displayName + " " + firebaseuser.photoUrl)
                        toastMessage(this, "" + firebaseuser.email + " " + firebaseuser.displayName + " " + firebaseuser.photoUrl)
                        selectUnitActivity(firebaseuser)
//                    google_user = user
//                    SaveUserToDB(user)
                    }
                }else{
                    toastMessage(this, "Google Firebase is not successfully signed in")
                    UpdateUI(context = this)
                }
            }
        }catch (e:ApiException){Log.d(TAG, ""+e.message)}

    }

    private fun createUserwithEmailPassword(){

        val edittextArray = arrayOf(user_name, email, password, confirm_password_)
        val spinnersArray = arrayOf(select_branch_name_spinner, select_unit_name_sign_spinner)
        val validResult = validator.validateEditText(edittextArray)
        val validSpinnerResult = validator.validateSpinners(this, spinnersArray)

        if (!validResult && !validSpinnerResult){
            branch_name = select_branch_name_spinner.selectedItem.toString()
            unit_name = select_unit_name_sign_spinner.selectedItem.toString()
            val email_text = email.text.toString()
            val pass = password.text.toString()
            val username = user_name.text.toString()
            val pass_confirm = confirm_password_.text.toString()

            if (validator.regex(validator.emailPattern, email_text)){
                if (validator.passwordMatch(pass, pass_confirm)){
                    if (pass.length >= 6){
                        Dialog.setTitle("Registration")
                        Dialog.setMessage("Verifying your signup details")
                        Dialog.setCanceledOnTouchOutside(false)
                        Dialog.show()

                        firebaseSignupwithEmailPassword(email_text, pass, username)

                    } else setErrorRequired(password, "Password must be at least 6 characters")
                } else {
                    setErrorRequired(password, "Password don't match")
                    setErrorRequired(confirm_password_, "Password don't match")
                }
            } else{
                setErrorRequired(email, "Email is invalid ")
            }

        }

    }

    private fun firebaseSignupwithEmailPassword(email:String, password:String, username:String){
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    Log.d("CREATE_USER_AND_PASSWORD", ""+task.result + task.exception)
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        if (user != null) {
                            SaveUserToDB(email=user.email!!, password=password, display_name = username,
                                user_id = user.uid, branch_name= branch_name, unit_name = unit_name)
                            UpdateUI(context = this, user=user)
                            Dialog.dismiss()
                            toastMessage(this, "Firebase is successfully signed in")

                            Log.d("CREATE_USER_AND_PASSWORD", "" + user.email)
                        }else toastMessage(this, "Firebase is not successfully signed in")
                    }else{
                        UpdateUI(context = this)
                    }
                }
        }catch (Error:Exception) {
            UpdateUI(context = this)
            Log.d("TAG_FORM_SIGNUP", "" + Error.message)
        }

    }


    fun signOut() = FirebaseAuth.getInstance().signOut()

    companion object{

        val onSuccessAction:() -> Unit = { println("User Created Successfully") }
        val onFailureAction:() -> Unit = { println("User not Created") }
        val USER_EMAIL = "USER_EMAIL"
        val USER_DISPLAYNAME = "USER_DISPLAYNAME"
        val USER_PASSWORD = "USER_PASSWORD"
        val USER_ID = "USER_ID"
        private lateinit var google_user:FirebaseUser

        fun SaveUserToDB(email: String, password: String, display_name: String, user_id:String, branch_name: String, unit_name: String){
            val userObj = User(email=email, password=password, username=display_name, key = user_id)
            try {
                realtimeDatabaseManager.saveUser(userObj, branch_name, unit_name, onSuccessAction, onFailureAction)
                Log.d(TAG, "${branch_name} ${unit_name} ${email} ${password} ${display_name} ")
            }catch (e:Exception){Log.d(TAG, e.message)}
        }

    }

}