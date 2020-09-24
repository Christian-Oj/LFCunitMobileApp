package com.winners.lfcunit

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDateTime

fun toastMessage(context: Context, message:String){
    val toast = Toast.makeText(context,message,Toast.LENGTH_LONG)
    toast.setGravity(Gravity.TOP or Gravity.LEFT, 0, 0)
    toast.show()
}

fun UpdateUI(user:FirebaseUser? = null, context: Context){
    toastMessage(context, "user is: "+user+ "am on UpdateUI function")
}

fun getCurrentTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun assignSpinnerAdapter(context: Context, array:Int): ArrayAdapter<CharSequence> {
    val adapter = ArrayAdapter.createFromResource(
        context,
        array,
        android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    return adapter
}

fun assignSpinnerListener(spinner: Spinner, array:Array<String>):String{
    var array_value = ""
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            array_value = array[position]
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
    return array_value
}

