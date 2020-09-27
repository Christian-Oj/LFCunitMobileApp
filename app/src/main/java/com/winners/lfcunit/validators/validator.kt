package com.winners.lfcunit.validators

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.winners.lfcunit.setErrorRequired
import com.winners.lfcunit.toastMessage

class validator {

    companion object{
        val emailPattern = "\\w+(.)?(\\w+)?(\\d+)?@\\w+.\\w{2,3}(.\\w{2,3})?"

        fun validateEditText(array:Array<EditText>):Boolean{
            for (textbox in array){
                if (textbox.text.isBlank()){
                    textbox.requestFocus()
                    textbox.setError("Required field")
                    return true
                }
            }

            return false
        }

        fun passwordMatch(pass1:String, pass2:String):Boolean{
            return pass1 == pass2
        }

        fun validateSpinners(context: Context, arr: Array<Spinner>):Boolean{
            val (branch_name, unit_name) = arr
            if (branch_name.selectedItem.toString() == "Select Church Branch") {
                val branch:TextView = branch_name.selectedView as TextView
                branch.apply {
                    requestFocus()
                    setError("")
                    setTextColor(Color.RED)
                    setText("Please select a branch")
                    return true
                }
            }

            if (unit_name.selectedItem.toString() == "Select Serving Unit") {
                val unit:TextView = unit_name.selectedView as TextView
                unit.apply {
                    requestFocus()
                    setError("")
                    setTextColor(Color.RED)
                    setText("Please select a branch")
                    return true
                }
            }
            return false
        }

        fun regex(pattern:String, para:String):Boolean{
            val regPattern = pattern.toRegex()
            if (regPattern.matches(para)) return true
            return false
        }

    }

}