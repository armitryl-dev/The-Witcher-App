package com.example.thewitcherapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formulariologin.Monstruo
import com.example.formulariologin.MonstruoAdapter
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentFavBinding
import com.example.thewitcherapp.databinding.FragmentListaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FragmentFav : Fragment() {

    private lateinit var binding: FragmentFavBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavBinding.inflate(layoutInflater)
        val view = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            espera()
        }

        val monstruoList = mutableListOf(
            Monstruo("Leshen", "Relicto", "Alta", R.drawable.leshen, false),
            Monstruo("Dettlaff", "Vampiro", "Extrema", R.drawable.supreme_vampire, false),
            Monstruo("Anciano", "Vampiro", "Máxima", R.drawable.ancient_elder, false),
            Monstruo("Ghoul", "Necrófago", "Baja", R.drawable.ghoul, false)
        )

        val adapter = MonstruoAdapter(requireContext(), monstruoList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                binding.swipeRefreshLayout.isRefreshing = true
                delay(2000) // Simulación de carga
                monstruoList.add(Monstruo("Ekimmara", "Vampiro", "Media", R.drawable.ekimmara, false))
                adapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private suspend fun espera(): String{

        binding.rv.visibility = View.INVISIBLE
        binding.progressBar!!.visibility = ProgressBar.VISIBLE

        for(i in 0..100){
            delay(30)
            binding.progressBar!!.progress = i
        }
        binding.progressBar!!.visibility = ProgressBar.GONE
        binding.rv.visibility = View.VISIBLE

        return "Carga completa"
    }

}
