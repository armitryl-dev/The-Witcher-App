package com.example.thewitcherapp.Fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentRegisterBinding

class FragmentRegister : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lógica de habilitar/deshabilitar el botón
        binding.botonSingUp.isEnabled = false

        // Validar campos al escribir
        binding.userNameEditText.addTextChangedListener { validateForm() }
        binding.emailEditText.addTextChangedListener { validateForm() }
        binding.setPasswordEditText.addTextChangedListener { validateForm() }
        binding.repeatPasswordEditText.addTextChangedListener { validateForm() }

        binding.botonSingUp.setOnClickListener {
            // Aquí puedes manejar la lógica de registro (como enviar los datos al servidor)
            Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_secondFragment_to_firstFragment)
        }
    }

    private fun validateForm() {
        val username = binding.userNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.setPasswordEditText.text.toString().trim()
        val repeatPassword = binding.repeatPasswordEditText.text.toString().trim()

        // Validar username, email, contraseñas
        val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty() && password == repeatPassword
        val isFieldsValid = username.isNotEmpty() && isEmailValid && isPasswordValid

        // Habilitar/deshabilitar el botón
        binding.botonSingUp.isEnabled = isFieldsValid

        // Mostrar errores dentro de los cuadros de texto
        if (username.isEmpty()) {
            binding.userNameEditText.error = getString(R.string.empty_username)
        } else {
            binding.userNameEditText.error = null
        }

        if (email.isEmpty() || !isEmailValid) {
            binding.emailEditText.error = getString(R.string.invalid_email)
        } else {
            binding.emailEditText.error = null
        }

        if (password.isEmpty() || password != repeatPassword) {
            binding.setPasswordEditText.error = getString(R.string.password_mismatch)
            binding.repeatPasswordEditText.error = getString(R.string.password_mismatch)
        } else {
            binding.setPasswordEditText.error = null
            binding.repeatPasswordEditText.error = null
        }
    }
}
