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
class FavoriteNoteViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {

    fun readAllFavoriteNotes(): LiveData<List<Note>> {
        return repository.readAllFavoriteNotes
    }
    fun removeAllNotesFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeAllNotesFromFavorites()
        }
    }

    fun sendAllFavoriteNotesToTrash(time_deleted: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendAllFavoriteNotesToTrash(time_deleted)
        }
    }
}