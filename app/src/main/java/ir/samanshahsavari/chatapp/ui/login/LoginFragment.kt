package ir.samanshahsavari.chatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import ir.samanshahsavari.chatapp.R
import ir.samanshahsavari.chatapp.databinding.FragmentLoginBinding
import ir.samanshahsavari.chatapp.ui.BindingFragment
import ir.samanshahsavari.chatapp.utils.Constants
import ir.samanshahsavari.chatapp.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    private val loginViewModel by viewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            setUpConnectingUIState()
            loginViewModel.connectUser(binding.etUsername.text.toString())
        }
        binding.etUsername.addTextChangedListener {
            binding.etUsername.error = null
        }

        subscribeToEvent()
    }

    private fun subscribeToEvent() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginEvent.collect { event ->
                when(event) {
                    is LoginViewModel.LoginEvent.Success -> {
                        setUpIdleUIState()
                        Toast.makeText(requireContext(), "Successful login", Toast.LENGTH_LONG).show()
                    }
                    is LoginViewModel.LoginEvent.ErrorLogin -> {
                        setUpIdleUIState()
                        Toast.makeText(requireContext(), event.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    is LoginViewModel.LoginEvent.ErrorInputTooShort -> {
                        setUpIdleUIState()
                        binding.etUsername.error = getString(R.string.error_username_too_short, Constants.MIN_USERNAME_LENGTH)
                    }
                }
            }
        }
    }

    private fun setUpConnectingUIState() {
        binding.progressBar.isVisible = true
        binding.btnConfirm.isEnabled = false
    }

    private fun setUpIdleUIState() {
        binding.progressBar.isVisible = false
        binding.btnConfirm.isEnabled = true
    }
}