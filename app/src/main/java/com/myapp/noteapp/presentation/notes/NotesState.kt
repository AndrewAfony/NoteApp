package com.myapp.noteapp.presentation.notes

import com.myapp.noteapp.domain.model.Note
import com.myapp.noteapp.domain.util.NoteOrder
import com.myapp.noteapp.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
