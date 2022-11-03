package com.judahben149.note.note.fragment.noteList


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentNoteDetailsBinding
import com.judahben149.note.hideKeyboard
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.viewmodel.NoteViewModel
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NoteDetailsFragment: Fragment() {

        private var _binding: FragmentNoteDetailsBinding? = null
        private val binding get() = _binding!!

        private val args by navArgs<com.judahben149.note.note.fragment.noteList.NoteDetailsFragmentArgs>()
        private lateinit var mViewmodel: NoteViewModel

        private var isNoteFavorite: Boolean = false
        private var isNoteDeleted: Boolean = false

        var timeCreated: String = ""

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
            setHasOptionsMenu(true)


//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                hideKeyboard()
//                updateNoteInDatabase(isNoteFavorite)
//                navigateToListFragment()
//            }
//        })
            return binding.root
        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            mViewmodel = ViewModelProvider(this).get(NoteViewModel::class.java)
            timeCreated = PrettyTime().format(Date(args.noteDetails.timeCreated))

            binding.noteTitleNoteDetailsScreen.setText(args.noteDetails.noteTitle)
            binding.noteBodyNoteDetailsScreen.setText(args.noteDetails.noteBody)
            binding.dateCreatedNoteDetailsScreen.text = "Created: $timeCreated"

            isNoteFavorite = args.noteDetails.favoriteStatus

//            binding.btnCancelNoteDetailsScreen.setOnClickListener {
//                hideKeyboard()
//                navigateToListFragment()
//            }
//
//            binding.btnSaveNoteNoteDetailsScreen.setOnClickListener {
//                updateNoteInDatabase(isNoteFavorite, false)
//                hideKeyboard()
//                navigateToListFragment()
//            }

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
            } else if (item.itemId == R.id.deleteNote){
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
                args.noteDetails.id,
                noteTitle,
                noteBody,
                favoriteStatus = isNoteFavorite,
                timeCreated = args.noteDetails.timeCreated,
                timeUpdated = timeUpdated,
                deletedStatus = isNoteDeleted,
                timeDeleted = timeUpdated
            )
            mViewmodel.updateNote(note)
        }

        private fun navigateToListFragment() {
            Navigation.findNavController(binding.root).navigate(R.id.action_noteDetailsFragment_to_noteListFragment)
        }
}