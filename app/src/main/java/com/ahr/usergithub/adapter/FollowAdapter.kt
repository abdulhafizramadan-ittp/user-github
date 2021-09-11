package com.ahr.usergithub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahr.usergithub.databinding.ItemFollowBinding
import com.bumptech.glide.Glide
import com.ahr.usergithub.model.User

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    private val listFollow = ArrayList<User>()

    fun setFollow(listFollow: ArrayList<User>) {
        this.listFollow.apply {
            clear()
            addAll(listFollow)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFollow[position])
    }

    override fun getItemCount(): Int = listFollow.size

    inner class ViewHolder(private val binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(ivPhoto)

                tvName.text = user.username
            }
        }
    }
}