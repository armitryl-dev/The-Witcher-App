package com.example.thewitcherapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.navigation.fragment.findNavController
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentLoginBinding

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //Solo se usará para inflar el layout del Fragment - ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.botonRegistrarse.setOnClickListener{
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

    }

    companion object {

        
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLogin().apply {

                }
    }
}