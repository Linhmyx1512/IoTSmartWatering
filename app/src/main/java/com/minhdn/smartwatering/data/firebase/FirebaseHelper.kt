package com.minhdn.smartwatering.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.utils.Constants.IS_PUMP_ON
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object FirebaseHelper {

    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    fun getDatabaseReference(path: String): DatabaseReference {
        return database.getReference(path)
    }

}