package com.judahben149.note.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.judahben149.note.getOrAwaitValue
import com.judahben149.note.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {

    private lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun addNote() = runBlockingTest {
        val noteItem = Note(
            1,
            "First Note",
            "First Note Body",
            favoriteStatus = false,
            privateStatus = false,
            0,
            0,
            false,
            0
        )
        dao.addNote(noteItem)
        val allNotes = dao.readAllNotes().getOrAwaitValue()

        assertThat(allNotes).contains(noteItem)
    }
}