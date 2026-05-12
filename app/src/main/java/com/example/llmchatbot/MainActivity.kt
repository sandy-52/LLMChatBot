package com.example.llmchatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.llmchatbot.ui.theme.LLMChatBotTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseHelper = ChatDatabaseHelper(this)

        setContent {
            LLMChatBotTheme {

                var username by remember {
                    mutableStateOf<String?>(null)
                }

                if (username == null) {

                    LoginScreen { enteredUsername ->
                        username = enteredUsername
                    }

                } else {

                    ChatScreen(
                        username = username!!,
                        databaseHelper = databaseHelper
                    )
                }
            }
        }
    }
}