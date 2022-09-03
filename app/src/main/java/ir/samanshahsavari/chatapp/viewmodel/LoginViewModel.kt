package ir.samanshahsavari.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import ir.samanshahsavari.chatapp.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUserName(userName: String) =
        userName.isNotEmpty() && userName.length >= Constants.MIN_USERNAME_LENGTH

    fun connectUser(userName: String) {
        val trimUserName = userName.trim()
        viewModelScope.launch {
            if (isValidUserName(userName)) {
                val result = client.connectGuestUser(
                    userId = trimUserName,
                    username = trimUserName
                ).await()
                if (result.isError) {
                    _loginEvent.emit(LoginEvent.ErrorLogin(result.error().message ?: "Unknown Error"))
                    return@launch
                }
                _loginEvent.emit(LoginEvent.Success)
            } else {
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }

    sealed class LoginEvent {
        object ErrorInputTooShort : LoginEvent()
        data class ErrorLogin(val errorMessage: String) : LoginEvent()
        object Success : LoginEvent()
    }
}