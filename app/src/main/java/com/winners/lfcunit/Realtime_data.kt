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
//    private user_list = MutableLiveData<List<User>>()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference(Database_Class)

    val changeListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (user_id != ""){
                is_unit_leader = snapshot.child(user_id).child("unitLeader").getValue(Boolean::class.java)!!
            }
            if (snapshot.hasChildren() && snapshot.childrenCount > 0){
                val changed_value = snapshot.getValue(String::class.java)
                users_array.add(User(email = "mm",username = "ddd", password = "fsf"))
            }
            if (snapshot.hasChild(USER_REFERENCE)) {
                // The snapshot contains a node at the specified path
            }

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    private fun createUser(user_key:String, user: User):SaveUser{
//        val auth_user = authenticationManager.getCurrentUser()
        val getCurrenttime = getCurrentTime()
        return SaveUser(user_key, user.email, user.password, user.username, getCurrenttime)
    }

    fun saveUser(user_obj:User,church_branch:String,unit_name:String, onSuccessAction:()->Unit, onFailureAction:()->Unit){
        val postUser = database.getReference(USER_REFERENCE)
        val user_key_ref = postUser.push().key?:""
//        val user = createUser(user_key_ref, user_obj)
        postUser.child(church_branch).child(unit_name).child(user_key_ref)
            .setValue(user_obj)
            .addOnSuccessListener { onSuccessAction() }
            .addOnFailureListener { onFailureAction() }
    }


    companion object{
        val users_array = arrayListOf<User>()
        var is_unit_leader = false
        var user_id = ""
    }

    fun assignListeners(branch:String, unit:String){
        val dbref = database.getReference("/${USER_REFERENCE}/${branch}/${unit}")
        dbref.addValueEventListener(changeListener)
    }

}