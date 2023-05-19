package com.aplikasi.turjawalichat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.turjawalichat.database.SharedPreference
import com.aplikasi.turjawalichat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var pref: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = SharedPreference(this)
        mAuth = FirebaseAuth.getInstance()

        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnMasuk.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        when {
            !isValidEmail(email) -> {
                binding.etEmail.error = "Email tidak valid"
                binding.etEmail.requestFocus()
            }
            email.isEmpty() -> {
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.requestFocus()
            }
            else -> {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            finish()
                            pref.setLoggin(true)
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            binding.etEmail.error = "Email atau password salah"
                            binding.etEmail.requestFocus()
                        }
                    }
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}