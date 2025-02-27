package com.example.formulariologin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.ItemLayoutBinding

class MonstruoAdapter(private val context: Context, private val monstruos: List<Monstruo>) : RecyclerView.Adapter<MonstruoAdapter.MonstruoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonstruoViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonstruoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonstruoViewHolder, position: Int) {
        holder.bind(monstruos[position])
    }

    override fun getItemCount(): Int {
        return monstruos.size
    }

    class MonstruoViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Monstruo) {
            binding.name.text = data.nombre
            binding.specie.text = data.especie
            binding.threat.text = data.amenaza
            binding.foto.setImageResource(data.foto)

            Glide
                .with(binding.root.context)
                .load(data.foto)
                .placeholder(R.drawable.google)
                .override(350, 350)
                .into(binding.foto);

            if (data.fav) {
                binding.fabFav.setImageResource(R.drawable.fav_selected)
            } else {
                binding.fabFav.setImageResource(R.drawable.fav_unselected)
            }

            binding.fabFav.setOnClickListener {
                if (data.fav) {
                    binding.fabFav.setImageResource(R.drawable.fav_unselected)
                } else {
                    binding.fabFav.setImageResource(R.drawable.fav_selected)
                }
                data.fav = !data.fav
            }

        }
    }
}

