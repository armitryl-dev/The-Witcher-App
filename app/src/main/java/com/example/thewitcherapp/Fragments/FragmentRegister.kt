package com.example.thewitcherapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentRegister : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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

        binding.botonSingUp.isEnabled = false

        binding.emailEditText.addTextChangedListener { validateForm() }
        binding.setPasswordEditText.addTextChangedListener { validateForm() }
        binding.repeatPasswordEditText.addTextChangedListener { validateForm() }

        binding.botonSingUp.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.setPasswordEditText.text.toString().trim()
            val user = binding.userNameEditText.text.toString().trim()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result?.user

                        val userMap = hashMapOf(
                            "email" to email,
                            "password" to password,
                            "user" to user
                        )

                        firestore.collection("usuarios")
                            .document(firebaseUser?.uid ?: "")
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_register_to_login)
                            }
                            .addOnFailureListener {
                                builder.setTitle("Error")
                                    .setMessage("Error al guardar en Firestore.")
                                    .setPositiveButton(R.string.confirm, null)
                                builder.create().show()
                            }

                    } else {
                        builder.setTitle("Error")
                            .setMessage(R.string.registerError)
                            .setPositiveButton(R.string.confirm, null)
                        builder.create().show()
                    }
                }
        }
    }

    private fun validateForm() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.setPasswordEditText.text.toString().trim()
        val repeatPassword = binding.repeatPasswordEditText.text.toString().trim()

        val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotEmpty() && password == repeatPassword

        binding.botonSingUp.isEnabled = isEmailValid && isPasswordValid

        binding.emailEditText.error = if (isEmailValid) null else getString(R.string.invalid_email)
        binding.setPasswordEditText.error = if (isPasswordValid) null else getString(R.string.password_mismatch)
        binding.repeatPasswordEditText.error = if (isPasswordValid) null else getString(R.string.password_mismatch)
    }
}