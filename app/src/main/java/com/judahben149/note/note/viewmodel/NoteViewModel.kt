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

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllNotes: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        readAllNotes = repository.readAllNotes
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

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
    fun sendSingleNoteToTrash(time_deleted: Long, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendSingleNoteToTrash(time_deleted, id)
        }
    }

    fun sendAllNotesToTrash(time_deleted: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendAllNotesToTrash(time_deleted)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Note>> {
        return  repository.searchDatabase(searchQuery)
    }

    fun restoreNotesFromTrash() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreNotesFromTrash()
        }
    }

    fun addSingleNoteToFavorites(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSingleNoteToFavorites(id)
        }
    }
}