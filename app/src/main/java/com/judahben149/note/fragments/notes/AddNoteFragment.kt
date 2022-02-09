package com.judahben149.note.fragments.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentAddNoteBinding
import com.judahben149.note.hideKeyboard
import com.judahben149.note.model.Note
import com.judahben149.note.viewmodel.NoteViewModel

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewmodel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        mViewmodel = ViewModelProvider(this).get(NoteViewModel::class.java)


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    val titleText = binding.etNoteTitleAddNoteScreen.text.toString()
                    val bodyText = binding.etNoteBodyAddNoteScreen.text.toString()

                    if (titleText.trim().isNotEmpty() || bodyText.trim().isNotEmpty()) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.titleBackPressDialog))
                            .setMessage(resources.getString(R.string.bodyBackPressDialog))
                            .setNegativeButton(resources.getString(R.string.negativeButtonBackPressDialog)) { dialog, which ->
                                hideKeyboard()
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_addNoteFragment_to_noteListFragment)
                            }
                            .setPositiveButton(resources.getString(R.string.positiveButtonBackPressDialog)) { dialog, which ->
                                insertNoteToDatabase()
                            }
                            .show()
                    } else {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_addNoteFragment_to_noteListFragment)
                    }
                }
            })

        binding.fabSaveNoteAddNoteScreen.setOnClickListener {
//            hideKeyboard()
            insertNoteToDatabase()
        }

        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun insertNoteToDatabase() {
        val noteTitle = binding.etNoteTitleAddNoteScreen.text.toString()
        val noteBody = binding.etNoteBodyAddNoteScreen.text.toString()
        val timeCreated = System.currentTimeMillis()

        val note = Note(
            0,
            noteTitle,
            noteBody,
            favoriteStatus = false,
            privateStatus = false,
            timeCreated = timeCreated,
            timeUpdated = timeCreated,
            deletedStatus = false,
            timeDeleted = 0
        )
        mViewmodel.addNote(note)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_addNoteFragment_to_noteListFragment)
    }
}