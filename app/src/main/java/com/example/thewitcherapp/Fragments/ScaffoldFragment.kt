package com.example.thewitcherapp.Fragments

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentScaffoldBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class ScaffoldFragment : Fragment() {
    private lateinit var binding: FragmentScaffoldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScaffoldBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* TOOLBAR */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val typefaceToolbar = ResourcesCompat.getFont(requireContext(), R.font.main_font)
        val toolbarTitle = binding.toolbar.title
        val spannableTitle = SpannableString(toolbarTitle)
        spannableTitle.setSpan(typefaceToolbar?.let { TypefaceSpan(it) }, 0, spannableTitle.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        binding.toolbar.title = spannableTitle

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar, menu)
                menu.forEach { item ->
                    val spannable = SpannableString(item.title)
                    spannable.setSpan(typefaceToolbar?.let { TypefaceSpan(it) }, 0, spannable.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    item.title = spannable
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {

                        true
                    }
                    R.id.action_sort -> {

                        true
                    }
                    R.id.action_logout -> {
                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.action_scaffold_to_login)
                        activity?.finish()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /* DRAWERLAYOUT */
        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(FragmentContacto())
                    true
                }
                R.id.nav_list -> {
                    replaceFragment(FragmentLista())
                    true
                }
                R.id.nav_fav -> {
                    replaceFragment(FragmentFav())
                    true
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_scaffold_to_login)
                    activity?.finish()
                    true
                }
                else -> false
            }
        }

        // Conectamos el bottonNavigationMenu con el NavController
        val navController = findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        /* BOTTOM NAVIGATION MENU */
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.bnm_home -> FragmentContacto()
                R.id.bnm_list -> FragmentLista()
                R.id.bnm_fav -> FragmentFav()
                else -> null
            }

            if (fragment != null) {
                replaceFragment(fragment)
                true
            } else {
                false
            }
        }

        // Cargar Fragmento por defecto al abrir la app
        if (savedInstanceState == null) {
            replaceFragment(FragmentContacto())
        }

        // Cambiar fuente en el menú inferior
        val typefaceBottomNav = ResourcesCompat.getFont(requireContext(), R.font.cinzel)

        binding.bottomNavigation.menu.forEach { item ->
            val spannable = SpannableString(item.title)
            spannable.setSpan(typefaceBottomNav?.let { TypefaceSpan(it) }, 0, spannable.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            item.title = spannable
        }

        // Cambiar fuente en el Drawer
        val typefaceDrawerTitulo = ResourcesCompat.getFont(requireContext(), R.font.morpheus)
        val typefaceDrawerItems = ResourcesCompat.getFont(requireContext(), R.font.cinzel)

        binding.navigationView.menu.forEach { item ->
            val spannable = SpannableString(item.title)
            spannable.setSpan(typefaceDrawerTitulo?.let { TypefaceSpan(it) }, 0, spannable.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            item.title = spannable

            item.subMenu?.forEach { subItem ->
                val subSpannable = SpannableString(subItem.title)
                subSpannable.setSpan(typefaceDrawerItems?.let { TypefaceSpan(it) }, 0, subSpannable.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                subItem.title = subSpannable
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, fragment)
            .commit()
    }
}
