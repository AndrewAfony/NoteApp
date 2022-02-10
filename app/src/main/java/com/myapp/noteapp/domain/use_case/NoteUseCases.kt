package com.myapp.noteapp.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNote: GetNoteUseCase
)