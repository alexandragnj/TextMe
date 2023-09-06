package com.example.textme.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.textme.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent { HomeScreen() }
        }
    }

    @Composable
    fun HomeScreen(){
        val auth = FirebaseAuth.getInstance()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                auth.signOut()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }) {
                Text(text = getString(R.string.log_out_button))
            }
        }
    }
}