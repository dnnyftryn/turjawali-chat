package com.aplikasi.turjawalichat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.turjawalichat.database.SharedPreference
import com.aplikasi.turjawalichat.databinding.ActivityRegisterBinding
import com.aplikasi.turjawalichat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var pref: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        pref = SharedPreference(this)

        binding.btnDaftar.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confPassword = binding.etKonfirmasiPassword.text.toString()
        val nama = binding.etNama.text.toString()

        when {
            nama.isEmpty() -> {
                binding.etNama.error = "Nama tidak boleh kosong"
                binding.etNama.requestFocus()
            }
            email.isEmpty() -> {
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
            }
            !isValidEmail(email) -> {
                binding.etEmail.error = "Email tidak valid"
                binding.etEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.etPassword.error = "Password minimal 6 karakter"
                binding.etPassword.requestFocus()
            }
            password != confPassword -> {
                binding.etKonfirmasiPassword.error = "Password tidak sama"
                binding.etKonfirmasiPassword.requestFocus()
            }
            else -> {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        val uid = mAuth.currentUser?.uid
                        if (task.isSuccessful) {
                            addUserToDatabase(nama, email, uid)
                            finish()
                            pref.setLoggin(true)
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        } else {
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                            binding.etEmail.requestFocus()
                        }
                    }
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun addUserToDatabase(nama: String, email: String, uid: String?) {
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabase.child("users").child(uid!!).setValue(User(nama, email, uid))
    }
}