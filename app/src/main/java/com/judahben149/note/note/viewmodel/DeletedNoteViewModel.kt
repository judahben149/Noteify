package com.judahben149.note.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.judahben149.note.NoteDatabase
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeletedNoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllDeletedNotes: LiveData<List<Note>>
    private val repository: NoteRepository


    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        readAllDeletedNotes = repository.readAllDeletedNotes
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