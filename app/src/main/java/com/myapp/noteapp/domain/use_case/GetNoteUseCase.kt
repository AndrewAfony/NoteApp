package com.myapp.noteapp.domain.use_case

import com.myapp.noteapp.domain.model.Note
import com.myapp.noteapp.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}