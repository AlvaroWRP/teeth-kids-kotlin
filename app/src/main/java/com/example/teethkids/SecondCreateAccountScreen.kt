package com.example.teethkids

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.example.teethkids.databinding.SecondCreateAccountScreenActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SecondCreateAccountScreen : AppCompatActivity() {

    private lateinit var binding: SecondCreateAccountScreenActivityBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_create_account_screen_activity)
        binding = SecondCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding.btnContinue.setOnClickListener {

            showProgressBar()
            val name = binding.edtInputName.text.toString()
            val phone = binding.edtInputPhone.text.toString()

            val user = User(name, phone)
            if (uid !=null){

                databaseReference.child(uid).setValue(user).addOnCompleteListener {

                    if (it.isSuccessful){

                        uploadProfilePic()
                        val intent = Intent(this, ThirdCreateAccountScreen::class.java)
                        startActivity(intent)

                    }else{

                        hideProgressBar()
                        Toast.makeText(this, "Falha ao atualizar a foto de perfil", Toast.LENGTH_SHORT).show()

                    }

                }

            }else{

                Toast.makeText(this@SecondCreateAccountScreen, "Falha no cadastro, tente novamente", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun uploadProfilePic() {

        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.blank_pfp}")
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+firebaseAuth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {

            hideProgressBar()
            Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{

            hideProgressBar()
            Toast.makeText(this, "Falha ao atualizar o perfil", Toast.LENGTH_SHORT).show()

        }

    }

    private fun showProgressBar(){

        dialog = Dialog(this@SecondCreateAccountScreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    private fun hideProgressBar(){

        dialog.dismiss()

    }
}
