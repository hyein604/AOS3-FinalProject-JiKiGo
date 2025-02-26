package com.protect.jikigo.data.repo

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.protect.jikigo.data.model.Confirm
import java.util.UUID
import javax.inject.Inject

class ConfirmRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
){

    // Firestore에 데이터 추가
    fun addConfirmItem(item: Confirm) {
        val collectionRef = firestore.collection("Confirm")

        collectionRef.add(item)
    }

    fun uploadConfirmImage(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference

        val fileReference = storageReference.child("confirm/${UUID.randomUUID()}")
        fileReference.putFile(uri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { url ->
                    onSuccess(url.toString())
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}