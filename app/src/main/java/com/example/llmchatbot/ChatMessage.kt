package com.example.llmchatbot

data class ChatMessage(
    val id: Int = 0,
    val username: String,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

