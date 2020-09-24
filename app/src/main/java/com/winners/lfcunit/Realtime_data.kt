package com.winners.lfcunit

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val USER_REFERENCE = "Users"

private const val Database_Class = "REALTIME_DATA_CLASS"
class Realtime_data {
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference(Database_Class)

    private fun createUser(user_key:String, user: User):SaveUser{
//        val auth_user = authenticationManager.getCurrentUser()
        val getCurrenttime = getCurrentTime()
        return SaveUser(user_key, user.email, user.password, user.username, getCurrenttime)
    }

    fun saveUser(content_obj:User,church_branch:String,unit_name:String, onSuccessAction:()->Unit, onFailureAction:()->Unit){
        val postUser = database.getReference(USER_REFERENCE)
        val user_key_ref = postUser.push().key?:""
        val user = createUser(user_key_ref, content_obj)
        postUser.child(church_branch).child(unit_name).child(user_key_ref)
            .setValue(user)
            .addOnSuccessListener { onSuccessAction() }
            .addOnFailureListener { onFailureAction() }
    }

    private fun addListenertoUserChanges(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()){
                    val users_list = dataSnapshot.child("LFCIyekogba_Users")
                    Log.d(Database_Class, users_list.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d(Database_Class, error.toException().toString())
            }
        })

    }



}