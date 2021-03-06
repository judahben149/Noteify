package com.judahben149.note.note.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.judahben149.note.note.model.Note

@Dao
interface NoteDao {

    //methods for regular notes
    @Query("SELECT * FROM note_table WHERE deleted_status=0 ORDER BY id ASC")
    fun readAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE note_table SET deleted_status = 1,time_deleted= :time_deleted WHERE id = :id")
    suspend fun sendSingleNoteToTrash(time_deleted: Long, id: Int)

    @Query("UPDATE note_table SET deleted_status = 1,time_deleted= :time_deleted WHERE deleted_status = 0 ")
    suspend fun sendAllNotesToTrash(time_deleted: Long)

    @Query("SELECT * FROM note_table WHERE deleted_status = 0 AND (note_title LIKE :searchQuery OR note_body LIKE :searchQuery)")
    fun searchDatabase(searchQuery: String): LiveData<List<Note>>


    //methods for favorite notes
    @Query("SELECT * FROM note_table WHERE favorite_status = 1 AND deleted_status = 0")
    fun readAllFavoriteNotes(): LiveData<List<Note>>

    @Query("UPDATE note_table SET favorite_status = 1 WHERE id = :id AND deleted_status = 0")
    suspend fun addSingleNoteToFavorites(id: Int)

    @Query("UPDATE note_table SET favorite_status = 0 WHERE favorite_status = 1")
    suspend fun removeAllNotesFromFavorites()

    @Query("UPDATE note_table SET deleted_status = 1,time_deleted= :time_deleted WHERE favorite_status = 1 ")
    suspend fun sendAllFavoriteNotesToTrash(time_deleted: Long)


    //methods for deleted notes
    @Query("SELECT * FROM note_table WHERE deleted_status = 1")
    fun readAllDeletedNotes(): LiveData<List<Note>>

    @Query("UPDATE note_table SET deleted_status = 0 WHERE deleted_status = 1")
    suspend fun restoreNotesFromTrash()

    @Query("DELETE FROM note_table WHERE deleted_status = 1")
    suspend fun deleteAllDeletedNotes()

}