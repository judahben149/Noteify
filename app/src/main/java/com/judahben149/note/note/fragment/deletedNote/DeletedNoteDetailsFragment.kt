package com.judahben149.note.note.fragment.deletedNote

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentDeletedNoteDetailsBinding
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.viewmodel.DeletedNoteViewModel
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class DeletedNoteDetailsFragment : Fragment() {

    private var _binding: FragmentDeletedNoteDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DeletedNoteDetailsFragmentArgs>()
    private lateinit var mViewmodel: DeletedNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletedNoteDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewmodel = ViewModelProvider(this).get(DeletedNoteViewModel::class.java)
        val timeCreated = PrettyTime().format(Date(args.deletedNote.timeCreated))

        binding.noteTitleDeletedNoteDetailsScreen.text = args.deletedNote.noteTitle
        binding.noteBodyDeletedNoteDetailsScreen.text = args.deletedNote.noteBody
        binding.timeCreatedDeletedNoteDetailsScreen.text = "Created: $timeCreated"

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.deleted_note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_restore_deleted_note) {
            restoreNote()
            Snackbar.make(binding.root, "Note restored", Snackbar.LENGTH_SHORT).show()
        } else if (item.itemId == R.id.menu_delete_note_permanently) {
            deleteNotePermanently()
            Snackbar.make(binding.root, "Note deleted permanently", Snackbar.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


//    override fun onPause() {
//        //this saves the note once the fragment loses focus or is going to be destroyed. Acts for Auto-save
//        updateNoteInDatabase(isNoteFavorite)
//        super.onPause()
//    }

    private fun restoreNote() {
        val noteToRestore = Note(
            args.deletedNote.id,
            args.deletedNote.noteTitle,
            args.deletedNote.noteBody,
            favoriteStatus = false,
            privateStatus = false,
            args.deletedNote.timeCreated,
            args.deletedNote.timeUpdated,
            deletedStatus = false
        )
        mViewmodel.updateNote(noteToRestore)
        navigateToListFragment()
        Snackbar.make(binding.root, "Note restored", Snackbar.LENGTH_SHORT).show()
    }

    private fun deleteNotePermanently() {
        val noteToDelete = Note(
            args.deletedNote.id,
            args.deletedNote.noteTitle,
            args.deletedNote.noteBody,
            favoriteStatus = false,
            privateStatus = false,
            args.deletedNote.timeCreated,
            args.deletedNote.timeUpdated,
            deletedStatus = false
        )
        mViewmodel.deleteNote(noteToDelete)
        navigateToListFragment()
        Snackbar.make(binding.root, "Note deleted permanently", Snackbar.LENGTH_SHORT).show()
    }



    private fun navigateToListFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.action_deletedNoteDetailsFragment_to_deletedNoteListFragment)
    }
}