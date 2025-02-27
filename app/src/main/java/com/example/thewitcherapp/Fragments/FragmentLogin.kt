package com.example.thewitcherapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.LinearLayout

class FragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
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

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

        fun isValidEmail(email: String): Boolean {
            return emailPattern.matches(email)
        }

        binding.botonLogin.isEnabled = false

        binding.userTextField2.addTextChangedListener { text ->
            val email = text.toString().trim()
            if (!isValidEmail(email)) {
                binding.userTextField2.error = getString(R.string.invalid_email)
            } else {
                binding.userTextField2.error = null
            }

            val password = binding.passwordTextField2.text.toString().trim()
            binding.botonLogin.isEnabled = isValidEmail(email) && password.isNotEmpty()
        }

        binding.passwordTextField2.addTextChangedListener { text ->
            val password = text.toString().trim()
            val email = binding.userTextField2.text.toString().trim()

            binding.botonLogin.isEnabled = isValidEmail(email) && password.isNotEmpty()
        }

        binding.botonLogin.setOnClickListener {
            val email = binding.userTextField2.text.toString().trim()
            val builder = AlertDialog.Builder(requireContext())

            if (!isValidEmail(email)) {
                binding.userTextField2.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.userTextField2.text.toString(), binding.passwordTextField2.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_to_scaffold)
                } else {
                    builder.setTitle("Error").setMessage(R.string.loginError).setPositiveButton(R.string.confirm, null)
                    builder.create().show()
                }
            }

        }

        binding.botonRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.textContrasenaOlvidada.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL

            val inputEmail = EditText(requireContext()).apply {
                hint = getString(R.string.emailHint)
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            layout.addView(inputEmail)

            builder.setTitle(R.string.resetPassword)
                .setView(layout)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    val email = inputEmail.text.toString().trim()

                    if (email.isNotEmpty()) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), getString(R.string.passwordResetSucces), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(requireContext(), getString(R.string.passwordResetFailed), Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.emptyPasswordReset), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create().show()
        }
    }
}
