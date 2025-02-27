package com.example.thewitcherapp.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.thewitcherapp.databinding.FragmentContactoBinding
import com.google.android.material.snackbar.Snackbar

class FragmentContacto : Fragment() {
    private lateinit var binding: FragmentContactoBinding

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
        const val REQUEST_CALL_PERMISSION = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactoBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }

    private fun llamar(numero: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numero"))
        startActivity(intent)
    }

    private fun mandarCorreo(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewTelefono.setOnClickListener {
            val numeroLlamar = binding.textViewTelefono.text.toString()
            if (tienePermisoLlamada()) {
                llamar(numeroLlamar)
            } else {
                preguntarPermisoLlamada()
            }
        }

        binding.imageViewEmail.setOnClickListener {
            val correo = binding.textViewEmail.text.toString()
            mandarCorreo(correo)
        }

        binding.imageViewLocation.setOnClickListener {
            if (tienePermisoLocalizacion()) {
                abrirMapa()
            } else {
                preguntarPermisos()
            }
        }

        binding.imageViewWhatsApp.setOnClickListener {
            val numero = binding.textViewTelefono.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$numero")
            startActivity(intent)
        }

    }

    private fun abrirMapa() {
        //genero un mapa, se lo asigno a una uri e inicio un intent
        val uri = Uri.parse("https://www.google.com/maps/place/40%C2%B022'08.6%22N+3%C2%B044'35.2%22W/@40.3690502,-3.7431075,15z/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun tienePermisoLocalizacion(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), "android.permission.ACCESS_FINE_LOCATION"
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun tienePermisoLlamada(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), "android.permission.CALL_PHONE"
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun preguntarPermisos() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf("android.permission.ACCESS_FINE_LOCATION"),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun preguntarPermisoLlamada() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf("android.permission.CALL_PHONE"),
            REQUEST_CALL_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirMapa()
                } else {
                    Snackbar.make(
                        binding.root,
                        "Permiso de ubicación denegado. No se puede abrir el mapa.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            REQUEST_CALL_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val numeroLlamar = binding.textViewTelefono.text.toString()
                    llamar(numeroLlamar)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Permiso de llamada denegado. No se puede realizar la llamada.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }


    }
}
