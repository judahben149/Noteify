package com.judahben149.note.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedNoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {


    fun readAllDeletedNotes(): LiveData<List<Note>> {
        return repository.readAllDeletedNotes
    }

    //methods for deleted notes
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun deleteAllDeletedNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllDeletedNotes()
        }
    }

    fun restoreNotesFromTrash() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreNotesFromTrash()
        }
    }
}