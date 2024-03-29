package com.judahben149.note.note.fragment.deletedNote

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentDeletedNoteListBinding
import com.judahben149.note.note.adapter.DeletedNoteListAdapter
import com.judahben149.note.note.viewmodel.DeletedNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletedNoteListFragment : Fragment() {

    private var _binding: FragmentDeletedNoteListBinding? = null
    private val binding get() = _binding!!
    private val adapter = DeletedNoteListAdapter()
    private lateinit var rvList: RecyclerView
    val mViewModel: DeletedNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletedNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvList = binding.rvDeletedNotesList
        rvList.adapter = adapter

        setupRecyclerViewLayout()
        setUpObservers()
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.deleted_notes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_restoreAllNotes -> {
                restoreNotesDialog()
            }
            R.id.menu_deleteAllNotes -> {
                deleteNotesForeverDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerViewLayout() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false).apply {
            rvList.layoutManager = this
        }
    }


    private fun setUpObservers() {
        mViewModel.readAllDeletedNotes().observe(viewLifecycleOwner) { note ->
            adapter.setData(note)
            hideOrShowPlaceholder()
        }
    }

    private fun deleteNotesForeverDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {

            setPositiveButton("Delete forever") { _, _ ->
                mViewModel.deleteAllDeletedNotes()
                Snackbar.make(binding.root, "Trash has been emptied", Snackbar.LENGTH_LONG)
                    .show()
            }
            setNegativeButton("Cancel") { _, _ ->
            }

            setTitle("Delete notes permanently")
            setMessage("Are you sure you want to empty the trash? Notes will be deleted forever.")
            setIcon(R.drawable.ic_delete)
            create()
            show()
        }
    }

    private fun restoreNotesDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {

            setPositiveButton("Restore") { _, _ ->
                mViewModel.restoreNotesFromTrash()
                Snackbar.make(binding.root, "Restoring all notes", Snackbar.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancel") { _, _ ->
            }

            setTitle("Restore notes from trash")
            setMessage("Are you sure you want to restore notes? Notes will be returned to note list.")
            setIcon(R.drawable.ic_restore)
            create()
            show()
        }
    }

    private fun hideOrShowPlaceholder() {
        if (adapter.itemCount < 1) {
            binding.trashPlaceholder.visibility = View.VISIBLE
        } else {
            binding.trashPlaceholder.visibility = View.INVISIBLE
        }
    }
}