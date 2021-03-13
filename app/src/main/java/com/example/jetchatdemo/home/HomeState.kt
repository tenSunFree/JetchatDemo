package com.example.jetchatdemo.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf

class HomeState(
    initialMessages: List<Message>
) {
    private val _messages: MutableList<Message> =
        mutableStateListOf(*initialMessages.toTypedArray())
    val messages: List<Message> = _messages
    fun addMessage(msg: Message) {
        _messages.add(msg)
    }
}

@Immutable
data class Message(
    val author: String,
    val content: String
)
