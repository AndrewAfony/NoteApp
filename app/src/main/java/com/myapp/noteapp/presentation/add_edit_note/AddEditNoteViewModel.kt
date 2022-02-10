package com.myapp.noteapp.presentation.add_edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.noteapp.domain.model.InvalidNoteException
import com.myapp.noteapp.domain.model.Note
import com.myapp.noteapp.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var noteTitle by mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    private set

    var noteContent by mutableStateOf(NoteTextFieldState(
        hint = "Enter some text"
    ))
    private set

    var noteColor by mutableStateOf(Note.noteColors.random().toArgb())

    var eventFlow = MutableSharedFlow<UIEvent>()
    private set

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        noteTitle = noteTitle.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        noteContent = noteContent.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        noteColor = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                noteTitle = noteTitle.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitle = noteTitle.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                noteContent = noteContent.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                noteContent = noteContent.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                noteColor = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.text,
                                content = noteContent.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor,
                                id = currentNoteId
                            )
                        )
                        eventFlow.emit(UIEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        eventFlow.emit(UIEvent.ShowSnackbar(
                            message = e.message ?: "Unknown error! Couldn't save not!"
                        ))
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String): UIEvent()
        object SaveNote: UIEvent()
    }
}