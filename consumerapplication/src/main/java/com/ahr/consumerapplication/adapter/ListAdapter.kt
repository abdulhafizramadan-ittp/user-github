package com.ahr.consumerapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahr.consumerapplication.databinding.ItemUserBinding
import com.ahr.consumerapplication.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val listUser = ArrayList<User>()

    fun setListUser(listUser: ArrayList<User>) {
        this.listUser.apply {
            clear()
            addAll(listUser)
            notifyDataSetChanged()
        }
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
        }
    }
}