package com.myapp.noteapp.domain.use_case

import com.myapp.noteapp.domain.model.Note
import com.myapp.noteapp.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}