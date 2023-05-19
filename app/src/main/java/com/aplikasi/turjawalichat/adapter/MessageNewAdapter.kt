package com.aplikasi.turjawalichat.adapter

import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.turjawalichat.databinding.ItemMessageBinding
import com.aplikasi.turjawalichat.databinding.ItemUserBinding
import com.aplikasi.turjawalichat.model.Message
import com.bumptech.glide.Glide

class MessageNewAdapter (private val currentIdUser : String) : RecyclerView.Adapter<MessageNewAdapter.ViewHolder>() {
    private var listMessage = ArrayList<Message>()

    fun setData(newListMessage : List<Message>?) {
        if (newListMessage == null) return
        listMessage.clear()
        listMessage.addAll(newListMessage)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMessageBinding.bind(itemView)
        fun bind(item : Message) {

            binding.tvMessage.movementMethod = LinkMovementMethod.getInstance()
            binding.tvMessage2.movementMethod = LinkMovementMethod.getInstance()
            if (currentIdUser == item.senderId && item.senderId != null) {
                binding.pengirim.visibility = View.GONE
                binding.pengirim2.visibility = View.VISIBLE
                if(isUrlValid(item.message)){
                    binding.lyImage2.visibility = View.VISIBLE
                    binding.triangleRedImg.visibility = View.VISIBLE
                    binding.tvTimestampImg2.visibility = View.VISIBLE
                    binding.tvMessage2.visibility = View.GONE
                    binding.triangleRed.visibility = View.GONE
                    binding.tvTimestamp2.visibility = View.GONE
                    Glide.with(itemView.context.applicationContext)
                        .load(item.message)
                        .into(binding.msImg2)
                    if (item.timestamp != null){
                        binding.tvTimestampImg2.setReferenceTime(item.timestamp)
                    }
                } else {
                    binding.lyImage2.visibility = View.GONE
                    binding.triangleRedImg.visibility = View.GONE
                    binding.tvTimestampImg2.visibility = View.GONE
                    binding.tvMessage2.text = item.message
                    binding.tvMessage2.visibility = View.VISIBLE
                    binding.triangleRed.visibility = View.VISIBLE
                    binding.tvTimestamp2.visibility = View.VISIBLE
                    if (item.timestamp != null) {
                        binding.tvTimestamp2.setReferenceTime(item.timestamp)
                    }
                }
            } else {
                binding.pengirim2.visibility = View.GONE
                binding.pengirim.visibility = View.VISIBLE
                if(isUrlValid(item.message)){
                    binding.lyImage.visibility = View.VISIBLE
                    binding.triangleBlueImg.visibility = View.VISIBLE
                    binding.tvTimestampImg.visibility = View.VISIBLE
                    binding.tvMessage.visibility = View.GONE
                    binding.triangleBlue.visibility = View.GONE
                    binding.tvTimestamp.visibility = View.GONE
                    Glide.with(itemView.context.applicationContext)
                        .load(item.message)
                        .into(binding.msImg)
                    if (item.timestamp != null){
                        binding.tvTimestampImg.setReferenceTime(item.timestamp)
                    }
                } else {
                    binding.lyImage.visibility = View.GONE
                    binding.triangleBlueImg.visibility = View.GONE
                    binding.tvTimestampImg.visibility = View.GONE
                    binding.tvMessage.text = item.message
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.triangleBlue.visibility = View.VISIBLE
                    binding.tvTimestamp.visibility = View.VISIBLE
                    if (item.timestamp != null) {
                        binding.tvTimestamp.setReferenceTime(item.timestamp)
                    }
                }
            }
        }
    }
    fun isUrlValid(url: String?): Boolean {
        url ?: return false
        return Patterns.WEB_URL.matcher(url).matches() && URLUtil.isValidUrl(url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listMessage[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listMessage.size
}