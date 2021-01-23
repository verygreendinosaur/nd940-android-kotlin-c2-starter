package com.udacity.asteroidradar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidViewBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapter(private val clickListener: AsteroidListener) : RecyclerView.Adapter<AsteroidAdapter.ViewHolder>() {

    class ViewHolder private constructor(val binding: AsteroidViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: Asteroid) {
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidViewBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    var data = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.asteroid = item
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

}
