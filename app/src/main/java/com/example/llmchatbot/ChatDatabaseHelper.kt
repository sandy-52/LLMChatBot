package com.example.llmchatbot

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChatDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "chat_database", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE chat_messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                message TEXT,
                isUser INTEGER,
                timestamp INTEGER
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS chat_messages")
        onCreate(db)
    }

    fun insertMessage(chatMessage: ChatMessage) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("username", chatMessage.username)
            put("message", chatMessage.message)
            put("isUser", if (chatMessage.isUser) 1 else 0)
            put("timestamp", chatMessage.timestamp)
        }

        db.insert("chat_messages", null, values)
    }

    fun getMessages(username: String): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM chat_messages WHERE username = ? ORDER BY timestamp ASC",
            arrayOf(username)
        )

        while (cursor.moveToNext()) {
            messages.add(
                ChatMessage(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    message = cursor.getString(cursor.getColumnIndexOrThrow("message")),
                    isUser = cursor.getInt(cursor.getColumnIndexOrThrow("isUser")) == 1,
                    timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"))
                )
            )
        }

        cursor.close()
        return messages
    }
}

