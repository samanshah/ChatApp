package ir.samanshahsavari.chatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication : Application() {

    @Inject
    lateinit var client: ChatClient

    override fun onCreate() {
        super.onCreate()
        //initialize stream sdk
        ChatDomain.Builder(client, applicationContext).build()
    }
}