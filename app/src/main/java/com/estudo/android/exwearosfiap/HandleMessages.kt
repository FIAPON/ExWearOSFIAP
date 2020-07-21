package com.estudo.android.exwearosfiap

import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HandleMessages : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            Looper.prepare()

            Toast.makeText(this, remoteMessage.data["place"], Toast.LENGTH_LONG).show()

            Looper.loop()
        }
    }
}
