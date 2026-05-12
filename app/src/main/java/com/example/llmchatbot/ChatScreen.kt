package com.example.llmchatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    username: String,
    databaseHelper: ChatDatabaseHelper
) {

    var messages by remember {
        mutableStateOf(databaseHelper.getMessages(username))
    }

    var inputMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF20D6F5),
                        Color(0xFF007BFF)
                    )
                )
            )
            .padding(12.dp)
    ) {

        Text(
            text = "Welcome $username!",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            items(messages) { message ->
                MessageBubble(message)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = inputMessage,
                onValueChange = {
                    inputMessage = it
                },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Type a message")
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {

                    val text = inputMessage.trim()

                    if (text.isNotEmpty()) {

                        val userMessage = ChatMessage(
                            username = username,
                            message = text,
                            isUser = true
                        )

                        databaseHelper.insertMessage(userMessage)

                        val botReply = ChatMessage(
                            username = username,
                            message = getBotReply(text),
                            isUser = false
                        )

                        databaseHelper.insertMessage(botReply)

                        messages = databaseHelper.getMessages(username)

                        inputMessage = ""
                    }
                }
            ) {
                Text("▶")
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {

    val alignment =
        if (message.isUser) Alignment.End
        else Alignment.Start

    val bubbleColor =
        if (message.isUser) Color.LightGray
        else Color.White

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {

        Box(
            modifier = Modifier
                .padding(6.dp)
                .background(
                    bubbleColor,
                    RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {

            Column {

                Text(message.message)

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatTimestamp(message.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {

    val formatter =
        SimpleDateFormat("hh:mm a", Locale.getDefault())

    return formatter.format(Date(timestamp))
}

fun getBotReply(userMessage: String): String {

    return when {

        userMessage.contains("hello", true) ->
            "Hello! How can I help you today?"

        userMessage.contains("hi", true) ->
            "Hi there!"

        userMessage.contains("name", true) ->
            "I am your AI chatbot."

        userMessage.contains("help", true) ->
            "Sure! Ask me anything."

        else ->
            "You said: $userMessage"
    }
}