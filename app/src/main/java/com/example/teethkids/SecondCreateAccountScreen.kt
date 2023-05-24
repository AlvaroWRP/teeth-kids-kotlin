package com.example.teethkids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.teethkids.databinding.SecondCreateAccountScreenActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecondCreateAccountScreen : AppCompatActivity() {

    lateinit var binding: SecondCreateAccountScreenActivityBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_create_account_screen_activity)
        binding = SecondCreateAccountScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = intent.getStringExtra("userId").toString()

        binding.btnContinue.setOnClickListener {
            saveAddress()
        }

        binding.btnGoBack.setOnClickListener {
            val pass = Intent(this, FirstCreateAccountScreen::class.java)
            startActivity(pass)
        }

    }

    private fun saveAddress() {
        val street = binding.edtInputStreet.text.toString()
        val number = binding.edtInputNumber.text.toString()
        val comp = binding.edtInputComp.text.toString()
        val street2 = binding.edtInputStreet2.text.toString()
        val number2 = binding.edtInputNumber2.text.toString()
        val comp2 = binding.edtInputComp2.text.toString()
        val street3 = binding.edtInputStreet3.text.toString()
        val number3 = binding.edtInputNumber3.text.toString()
        val comp3 = binding.edtInputComp3.text.toString()

        if (street.isNotEmpty() && number.isNotEmpty() && comp.isNotEmpty()) {

            // Save address to Firestore
            val firstAddress = hashMapOf(
                "address1" to "$street, $number, $comp",
            )

            val secondAddress = hashMapOf(
                "address2" to "$street2, $number2, $comp2",
            )

            val thirdAddress = hashMapOf(
                "address2" to "$street3, $number3, $comp3",
            )


            firestore.collection("users")
                .document(userId)
                .update(
                    "address1",
                    firstAddress,
                    "address2",
                    secondAddress,
                    "address3",
                    thirdAddress
                )
                .addOnSuccessListener {
                    Toast.makeText(this, "Endereco cadastrado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, ThirdCreateAccountActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Falha ao Cadastrar Endereco", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(this, "Ao menos um endereco e necessario para o cadastro", Toast.LENGTH_SHORT).show()
        }
    }

}
