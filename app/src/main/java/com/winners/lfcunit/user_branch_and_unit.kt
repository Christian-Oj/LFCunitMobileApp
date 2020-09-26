package com.winners.lfcunit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class user_branch_and_unit : AppCompatActivity(), View.OnClickListener {
    private lateinit var spinner_branch:Spinner
    private lateinit var spinner_unit:Spinner
    private lateinit var submit_branch_unit:Button
    private lateinit var branch_array:Array<String>
    private lateinit var unit_array:Array<String>
    private var intent_value: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_branch_and_unit)


        intent_value = intent.extras
        branch_array = resources.getStringArray(R.array.church_branch)
        unit_array = resources.getStringArray(R.array.units)

        spinner_branch = findViewById(R.id.select_branch)
        spinner_unit = findViewById(R.id.select_unit)
        submit_branch_unit = findViewById(R.id.submit_unit)
        submit_branch_unit.setOnClickListener(this)

        SpinnerAdapter()

    }

    private fun SpinnerAdapter() {
        spinner_unit.adapter = assignSpinnerAdapter(this, R.array.units)
        spinner_branch.adapter = assignSpinnerAdapter(this, R.array.church_branch)
    }

    companion object{
        lateinit var email:String
        lateinit var passord:String
        lateinit var displayname:String
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.submit_unit -> branch_unit_Class_saveUser()
            }
        }
    }

    private fun getValueFromGoogleSignUp(bundle: Bundle) {
        var user_array_values = arrayListOf<String>()
        email = bundle.getString(Signup.USER_EMAIL).toString()
        passord = bundle.getString(Signup.USER_PASSWORD).toString()
        displayname = bundle.getString(Signup.USER_DISPLAYNAME).toString()



    }

    private fun branch_unit_Class_saveUser() {
//        var user_branch:String=""
//        var user_unit:String=""
        val user_branch = spinner_branch.selectedItem.toString().replace(" ", "_")
        val user_unit = spinner_unit.selectedItem.toString().replace(" ", "_")

//        user_branch = assignSpinnerListener(spinner_branch, branch_array)

        if (intent_value != null) {
            getValueFromGoogleSignUp(intent_value!!)

//            toastMessage(this, " ${user_branch} ${user_unit} $email $passord $displayname")
            Log.d("TAG_UNIT_BRANCH", "$email $passord $displayname $user_branch $user_unit")
            Signup.SaveUserToDB(email, passord, displayname, user_branch, user_unit)
        }
    }
}