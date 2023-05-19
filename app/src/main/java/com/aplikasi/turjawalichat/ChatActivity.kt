package com.aplikasi.turjawalichat

import android.net.Uri
import android.nfc.NfcAdapter.EXTRA_DATA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.turjawalichat.adapter.MessageAdapter
import com.aplikasi.turjawalichat.databinding.ActivityChatBinding
import com.aplikasi.turjawalichat.model.Message
import com.aplikasi.turjawalichat.model.User
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var mDbRef: DatabaseReference
    private lateinit var adapter: MessageAdapter


    var receiverRoom: String? = null
    var senderRoom: String? = null
    private var imageUri: Uri? = null
    private var senderIdImage = ""

    companion object{
        const val EXTRA_DATA = "extra_data"
        const val REQUEST_IMAGE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDbRef = FirebaseDatabase.getInstance().getReference()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val dataUser = intent.getParcelableExtra<User>(EXTRA_DATA)
        showDataChat(dataUser)
    }

    private fun showDataChat(dataUser: User?) {
        dataUser?.let {
            title = dataUser.name
            val receiverUid = dataUser.uid
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            if (senderUid != null) {
                senderIdImage = senderUid
            }
            Log.d("senderUid", senderUid.toString())
            Log.d("receiverUid", receiverUid.toString())
            senderRoom = receiverUid + senderUid
            receiverRoom = senderUid + receiverUid

            Log.d("senderRoom", senderRoom.toString())
            Log.d("receiverRoom", receiverRoom.toString())
            getListMessage(senderUid)
            sendMessage(senderUid)
        }
    }

    private fun sendMessage(senderUid: String?){
        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            val messageObject = Message(message, senderUid, Date().time)
            if(binding.messageEditText.text.isEmpty()){
                binding.messageEditText.error = "ketika pesan kamu"
            } else {
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
            }
            binding.messageEditText.setText("")
        }
    }

    private fun getListMessage(senderUid: String?){
        val manager = LinearLayoutManager(this)
        binding.messageRecyclerView.itemAnimator = null
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        val messageRef =   mDbRef.child("chats").child(senderRoom!!).child("messages")
        Log.d("TAG", "getListMessage: $messageRef")
        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messageRef, Message::class.java)
            .build()
        Log.d("TAG", "options: ${options.snapshots}")
        adapter = MessageAdapter(options, senderUid!!)
        binding.messageRecyclerView.adapter = adapter
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val array = adapter.itemCount
                Log.d("TAG", "array: $array")
                Log.d("TAG", "snapshot: $snapshot")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}