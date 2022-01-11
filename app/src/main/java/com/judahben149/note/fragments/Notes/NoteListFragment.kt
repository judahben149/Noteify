package com.judahben149.note.fragments.Notes

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.R
import com.judahben149.note.adapters.NoteListAdapter
import com.judahben149.note.databinding.FragmentNoteListBinding
import com.judahben149.note.model.Note
import com.judahben149.note.viewmodel.NoteViewModel

class NoteListFragment : Fragment() {


    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: NoteViewModel
    val adapter = NoteListAdapter()

    private val noteObject = emptyList<Note>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rvList = binding.rvList
        rvList.adapter = adapter


        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            rvList.layoutManager = this
        }

        setUpViewModelAndObserver()
        recyclerViewDivider(rvList, layoutManager)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }
        })

        binding.fabAddNoteButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_noteListFragment_to_addNoteFragment)
//            Snackbar.make(binding.root, "Create note", Snackbar.LENGTH_SHORT).show()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }



    private fun setUpViewModelAndObserver() {
        //instantiate viewmodel and set up observer
        mViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        mViewModel.readAllNotes.observe(viewLifecycleOwner, { note ->
            adapter.setData(note)
        })
    }


    private fun recyclerViewDivider(rvList: RecyclerView, layoutManager: LinearLayoutManager) {
        //this adds the divider line in between each item
        DividerItemDecoration(requireContext(), layoutManager.orientation)
            .apply {
                rvList.addItemDecoration(this)
            }
    }



    //function to search for a particular string either in the title or body of the note
    fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }

        })
    }


    private fun deleteAllNotes() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {

            setPositiveButton("Yes") { _, _ ->
                mViewModel.sendAllNotesToTrash(System.currentTimeMillis())
                adapter.notifyDataSetChanged()
                Snackbar.make(binding.root, "Successfully deleted all notes", Snackbar.LENGTH_LONG)
                    .show()

            }
            setNegativeButton("No") { _, _ ->

            }

            setTitle("Delete all notes")
            setMessage("Are you sure you want to delete all notes? Notes will be moved to trash")
            setIcon(R.drawable.ic_delete)
            create()
            show()
        }
    }

    private fun undoDelete(position: Note) {
        mViewModel.addNote(position)
        adapter.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_deleteAllNotes) {
            if (adapter.itemCount > 0) {
                deleteAllNotes()
            } else {
                Snackbar.make(binding.root, "There is no note to delete", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}