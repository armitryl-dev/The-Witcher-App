package com.example.thewitcherapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
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
import com.example.thewitcherapp.databinding.FragmentRegisterBinding

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

            if (!isValidEmail(email)) {
                binding.userTextField2.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_login_to_scaffold)
        }

        binding.botonRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.textContrasenaOlvidada.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            val input = EditText(requireContext()).apply {
                hint = getString(R.string.resetPasswordHint)
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            builder.setTitle(R.string.resetPassword)
                .setView(input)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    val password = input.text.toString().trim()
                    if (password.isNotEmpty()) {
                        Toast.makeText(requireContext(), getString(R.string.passwordResetSucces), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.emptyPasswordReset), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }

            builder.create().show()
        }
    }
}
