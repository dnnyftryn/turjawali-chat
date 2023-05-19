package com.aplikasi.turjawalichat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.turjawalichat.databinding.ItemUserBinding
import com.aplikasi.turjawalichat.model.User
import java.util.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var listData = ArrayList<User>()

    var onItemClick: ((User) -> Unit)? = null

    fun setData(newListData: User?) {
        if (newListData == null) return
        listData.addAll(listOf(newListData))
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(data : User) {
            with(binding) {
                binding.tvName.text = data.name
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[absoluteAdapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size
}