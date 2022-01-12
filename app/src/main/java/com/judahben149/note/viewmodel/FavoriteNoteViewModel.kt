package com.judahben149.note.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.judahben149.note.data.NoteDatabase
import com.judahben149.note.model.Note
import com.judahben149.note.repository.NoteRepository

class FavoriteNoteViewModel(application: Application): AndroidViewModel(application) {

    private val repository: NoteRepository
    val readAllFavoriteNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        readAllFavoriteNotes = repository.readAllFavoriteNotes
    }
}