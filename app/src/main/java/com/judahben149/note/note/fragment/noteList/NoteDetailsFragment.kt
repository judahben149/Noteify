package com.judahben149.note.note.fragment.noteList


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentNoteDetailsBinding
import com.judahben149.note.hideKeyboard
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {


    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<com.judahben149.note.note.fragment.noteList.NoteDetailsFragmentArgs>()
    val mViewmodel: NoteViewModel by activityViewModels()

    private val navController by lazy { findNavController() }

    //other variables
    private var isNoteFavorite: Boolean = false
    private var isNoteDeleted: Boolean = false
    var timeCreated: String = ""

    lateinit var noteDetails: Note


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        mViewmodel.getNoteByID(args.noteId).observe(viewLifecycleOwner) { note ->
            noteDetails = note

            timeCreated = PrettyTime().format(Date(noteDetails.timeCreated))

            binding.noteTitleNoteDetailsScreen.setText(noteDetails.noteTitle)
            binding.noteBodyNoteDetailsScreen.setText(noteDetails.noteBody)
            binding.dateCreatedNoteDetailsScreen.text = "Created: $timeCreated"

            isNoteFavorite = noteDetails.favoriteStatus
        }


        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addToFavorites) {
            Snackbar.make(binding.root, "Note added to favorites", Snackbar.LENGTH_SHORT).show()
            isNoteFavorite = true
        } else if (item.itemId == R.id.deleteNote) {
            isNoteDeleted = true
//                updateNoteInDatabase(isNoteFavorite, isNoteDeleted)
            navigateToListFragment()
            Snackbar.make(binding.root, "Note added to trash", Snackbar.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPause() {
        //this saves the note once the fragment loses focus or is going to be destroyed. Acts for Auto-save
        hideKeyboard()
        updateNoteInDatabase(isNoteFavorite, isNoteDeleted)
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun updateNoteInDatabase(isNoteFavorite: Boolean, isNoteDeleted: Boolean) {
        val noteTitle = binding.noteTitleNoteDetailsScreen.text.toString()
        val noteBody = binding.noteBodyNoteDetailsScreen.text.toString()
        val timeUpdated = System.currentTimeMillis()

        val note = Note(
            noteDetails.id,
            noteTitle,
            noteBody,
            favoriteStatus = isNoteFavorite,
            timeCreated = noteDetails.timeCreated,
            timeUpdated = timeUpdated,
            deletedStatus = isNoteDeleted,
            timeDeleted = timeUpdated
        )
        mViewmodel.updateNote(note)
    }

    private fun navigateToListFragment() {
        navController.popBackStack()
    }
}