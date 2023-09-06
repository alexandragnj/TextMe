package com.example.textme.presentation.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.textme.R
import com.example.textme.ui.theme.blueBackground
import com.example.textme.ui.theme.orange
import com.example.textme.utils.CustomSnackBar
import com.example.textme.utils.CustomTextField
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent { LoginScreen() }
        }
    }

    override fun onStart() {
        super.onStart()
        checkIfUserSignedIn()
    }

    private fun checkIfUserSignedIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user?.uid != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun LoginScreen() {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val viewModel = get<AuthViewModel>()
        val coroutineScope = rememberCoroutineScope()
        val errorText = remember { mutableStateOf("") }
        val snackState = remember { SnackbarHostState() }

        ObserveViewModel(viewModel = viewModel, errorText = errorText)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = blueBackground)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.text_me),
                    contentDescription = "Logo",
                    Modifier.padding(10.dp)
                )
                Text(
                    text = getString(R.string.enter_account_text),
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp, color = Color.White
                )
                CustomTextField(label = getString(R.string.email_label), textFieldValue = email)
                CustomTextField(
                    label = getString(R.string.password_label),
                    textFieldValue = password,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        viewModel.signInWithEmailAndPassword(email.value, password.value)
                        coroutineScope.launch {
                            delay(100)
                            if (errorText.value.isNotEmpty()) {
                                snackState.showSnackbar(errorText.value)
                                errorText.value = ""
                            }
                        }
                    },
                    modifier = Modifier.padding(12.dp),
                    elevation = ButtonDefaults.elevation(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        orange
                    )
                ) {
                    Text(text = getString(R.string.login_text), color = Color.White)
                }
                Text(
                    text = getString(R.string.no_account_text),
                    modifier = Modifier.clickable { findNavController().navigate(R.id.registerFragment) },
                    color = Color.White
                )
                SnackbarHost(hostState = snackState) {
                    CustomSnackBar(text = errorText)
                }
            }
        }
    }

    @Composable
    fun ObserveViewModel(
        viewModel: AuthViewModel,
        errorText: MutableState<String>
    ) {
        viewModel.authResult.value.onSuccess {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
            .onFailure { error: AuthErrors ->
                errorText.value = error.getAuthError(requireContext())
            }
    }
}