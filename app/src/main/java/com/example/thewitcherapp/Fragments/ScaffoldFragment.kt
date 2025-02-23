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
import com.example.thewitcherapp.R
import com.example.thewitcherapp.databinding.FragmentScaffoldBinding
import com.google.android.material.navigation.NavigationView


class ScaffoldFragment : Fragment()
{
    private lateinit var binding: FragmentScaffoldBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentScaffoldBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        /* TOOLBAR */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val typefaceToolbar = ResourcesCompat.getFont(requireContext(), R.font.main_font)
        val toolbarTitle = binding.toolbar.title
        val spannableTitle = SpannableString(toolbarTitle)
        spannableTitle.setSpan(typefaceToolbar?.let { TypefaceSpan(it) }, 0, spannableTitle.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        binding.toolbar.title = spannableTitle

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
            {
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
                        // Manejar la selección del item1
                        true
                    }
                    R.id.action_settings -> {
                        // Manejar la selección del item2
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /* DRAWERLAYOUT */
        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {

                item -> when(item.itemId)
        {
            R.id.nav_home -> {
                true
            }

            R.id.nav_list -> {
                true
            }

            R.id.nav_notifications -> {
                true
            }

            else -> false
        }
        }

        /* BOTTOM NAVIGATION MENU */
        binding.bottomNavigation.setOnItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.bnm_home -> {
                    // Handle Home navigation
                    true
                }
                R.id.bnm_list -> {
                    // Handle Dashboard navigation
                    true
                }
                R.id.bnm_notifications -> {
                    // Handle Notifications navigation
                    true
                }
                else -> false
            }
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
}
