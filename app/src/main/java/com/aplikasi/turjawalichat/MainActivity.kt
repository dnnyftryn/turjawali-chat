package com.aplikasi.turjawalichat

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_DATA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.turjawalichat.adapter.UserAdapter
import com.aplikasi.turjawalichat.databinding.ActivityMainBinding
import com.aplikasi.turjawalichat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()

        getListUser()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            mAuth.signOut()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            return true
        }
        return true
    }

    private fun getListUser() {
        val userAdapter = UserAdapter()
        userAdapter.onItemClick = { selectedData ->
            val intent = Intent(this@MainActivity, ChatActivity::class.java)
            intent.putExtra(ChatActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }
        mDbRef.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid) {
                        userAdapter.setData(currentUser)
                    } else {
                        title = currentUser?.name
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        with(binding.rvUsers) {
            layoutManager =   LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }
}
