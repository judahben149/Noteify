package com.judahben149.note.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {


    val readAllNotes: LiveData<List<Note>> = repository.readAllNotes


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

    fun getNoteByID(noteId: Int): LiveData<Note> = repository.getNoteByID(noteId)


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