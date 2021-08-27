package com.example.usergithub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.usergithub.databinding.ItemUserBinding
import com.example.usergithub.model.User

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val listUser = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setListUser(listUser: ArrayList<User>) {
        this.listUser.apply {
            clear()
            addAll(listUser)
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(85, 85))
                    .into(ivPhoto)

                tvName.text = user.username
            }

            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}