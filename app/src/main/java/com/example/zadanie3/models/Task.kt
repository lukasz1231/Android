package com.example.zadanie3.models

import java.util.UUID
import java.util.Date

class Task(
    var name: String = "",
    var date: Date = Date(),
    var isDone: Boolean = false
) {
    val id: UUID = UUID.randomUUID()
}