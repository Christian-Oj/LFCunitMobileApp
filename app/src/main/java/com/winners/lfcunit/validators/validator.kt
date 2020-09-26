package com.winners.lfcunit.validators

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import com.winners.lfcunit.toastMessage

class validator {

    companion object{
        fun validateEditText(array:Array<EditText>):Boolean{
            for (textbox in array){
                if (textbox.text.isBlank()){
                    textbox.requestFocus()
                    textbox.setError("Required field")
                    Log.d("VALIDATOR_CLASS", ""+textbox.hint)
                    return true
                }
            }
//            array.forEach {
//                if (it.text.isBlank()){
//                    it.requestFocus()
//                    it.setError("Required field")
//                    Log.d("VALIDATOR_CLASS", ""+it.hint)
//                    return true
//                }
//                else return false
//            }

            return false
        }

        fun passwordMatch(pass1:String, pass2:String):Boolean{
            return pass1 == pass2
        }

        fun validateSpinners(context: Context, arr: Array<Spinner>):Boolean{
            arr.forEach {
                if (it.selectedItem.toString().isEmpty()) {
                    toastMessage(context, "Select your branch or Unit")
                    return true
                } else false
            }
            return false
        }
    }

}