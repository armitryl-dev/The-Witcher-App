package com.example.thewitcherapp.Fragments

import android.os.Bundle
import android.util.Log
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
import com.example.thewitcherapp.databinding.FragmentListaBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentLista : Fragment() {

    private lateinit var binding: FragmentListaBinding
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaBinding.inflate(layoutInflater)
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

        val monstruoList = mutableListOf<Monstruo>()
        val adapter = MonstruoAdapter(requireContext(), monstruoList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter

        cargarDatosFirestore(adapter, monstruoList)

        binding.swipeRefreshLayout.setOnRefreshListener {
            cargarDatosFirestore(adapter, monstruoList)
        }
    }

    private fun cargarDatosFirestore(adapter: MonstruoAdapter, monstruoList: MutableList<Monstruo>) {
        binding.swipeRefreshLayout.isRefreshing = true
        db.collection("monstruos")
            .get()
            .addOnSuccessListener { result ->
                monstruoList.clear()
                for (document in result) {
                    val nombre = document.getString("nombre") ?: ""
                    val especie = document.getString("especie") ?: ""
                    val amenaza = document.getString("amenaza") ?: ""
                    val foto = document.getString("foto") ?: ""
                    val fav = document.getBoolean("fav") ?: false

                    val resourceId = resources.getIdentifier(foto, "drawable", requireContext().packageName)

                    val monstruo = Monstruo(nombre, especie, amenaza, resourceId, fav)
                    monstruoList.add(monstruo)
                }
                adapter.notifyDataSetChanged()
                binding.swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { exception ->
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }

    private suspend fun espera(): String {
        binding.rv.visibility = View.INVISIBLE
        binding.progressBar!!.visibility = ProgressBar.VISIBLE

        for (i in 0..100) {
            delay(30)
            binding.progressBar!!.progress = i
        }
        binding.progressBar!!.visibility = ProgressBar.GONE
        binding.rv.visibility = View.VISIBLE

        return "Carga completa"
    }
}