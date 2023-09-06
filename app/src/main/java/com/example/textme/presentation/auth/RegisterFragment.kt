package com.example.textme.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.textme.R
import com.example.textme.ui.theme.blueBackground
import com.example.textme.ui.theme.orange
import com.example.textme.utils.CustomSnackBar
import com.example.textme.utils.CustomTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent { RegisterScreen() }
        }
    }

    @Composable
    fun RegisterScreen() {
        val firstName = remember {
            mutableStateOf("")
        }
        val lastName = remember {
            mutableStateOf("")
        }
        val email = remember {
            mutableStateOf("")
        }
        val password = remember {
            mutableStateOf("")
        }
        val passwordConfirmation = remember {
            mutableStateOf("")
        }
        val viewModel = get<AuthViewModel>()
        val coroutineScope = rememberCoroutineScope()
        val errorText = remember {
            mutableStateOf("")
        }
        val snackState = remember { SnackbarHostState() }

        ObserveRegisterViewModel(
            viewModel = viewModel,
            authError = errorText
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueBackground)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getString(R.string.create_account_text),
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                CustomTextField(
                    label = getString(R.string.first_name_label),
                    textFieldValue = firstName
                )
                CustomTextField(
                    label = getString(R.string.last_name_label),
                    textFieldValue = lastName
                )
                CustomTextField(label = getString(R.string.email_label), textFieldValue = email)
                CustomTextField(
                    label = getString(R.string.password_label),
                    textFieldValue = password,
                    visualTransformation = PasswordVisualTransformation()
                )
                CustomTextField(
                    label = getString(R.string.password_confirmation_label),
                    textFieldValue = passwordConfirmation,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        viewModel.signUpUserWithEmailAndPassword(
                            email.value,
                            password.value,
                            passwordConfirmation.value,
                            firstName.value,
                            lastName.value
                        )
                        coroutineScope.launch {
                            delay(100)
                            if (errorText.value.isNotEmpty()) {
                                snackState.showSnackbar(errorText.value)
                                errorText.value = ""
                            }
                        }
                    },
                    modifier = Modifier.padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        orange
                    )
                ) {
                    Text(text = getString(R.string.register_text), color = Color.White)
                }
                Text(
                    text = getString(R.string.register_already_have_an_account_text),
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .clickable { findNavController().popBackStack() })
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    SnackbarHost(
                        hostState = snackState,
                    ) {
                        CustomSnackBar(text = errorText)
                    }
                }
            }
        }

    }

    @Composable
    fun ObserveRegisterViewModel(
        viewModel: AuthViewModel,
        authError: MutableState<String>
    ) {
        viewModel.authResult.value.onSuccess {
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }.onFailure { error: AuthErrors ->
            authError.value = error.getAuthError(requireContext())
        }
    }
}