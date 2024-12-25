package com.learning.careerconnect.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.SocketHandler

class ChatActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        SocketHandler.setSocket("123")
        SocketHandler.establishConnection()
        val counterBtn = findViewById<Button>(R.id.counterBtn)
        val countTextView = findViewById<TextView>(R.id.countTextView)

        val mSocket = SocketHandler.getSocket()

        counterBtn.setOnClickListener{
            mSocket.emit("chat message",
                "123",
                "456",
                "hii",
                "12"
            )
        }

        mSocket.on("chat message") { args ->
            if (args.isNotEmpty()) {
                val receivedMessage = args[0]
                runOnUiThread {
                    countTextView.text = receivedMessage.toString()
                }
            }
        }
    }
}