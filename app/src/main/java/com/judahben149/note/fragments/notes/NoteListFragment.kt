package com.judahben149.note.fragments.notes

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.LongPressed
import com.judahben149.note.R
import com.judahben149.note.adapters.NoteListAdapter
import com.judahben149.note.databinding.FragmentNoteListBinding
import com.judahben149.note.viewmodel.NoteViewModel

private const val TAG = "NoteListFragment"

class NoteListFragment : Fragment(), LongPressed {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: NoteViewModel
    private lateinit var adapter: NoteListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView")

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.d(TAG, "onViewCreated")
        adapter = NoteListAdapter(requireContext(), this)

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
        }

        setUpFloatingActionButton()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        _binding = null
        super.onDestroyView()
    }


    override fun onResume() {
        hideOrShowSearchViewAndPlaceholder()
        Log.d(TAG, "onResume")
        super.onResume()
    }

    private fun setUpFloatingActionButton() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { // Scrolling down
                    binding.fabAddNoteButton.shrink()
                } else { // Scrolling up
                    binding.fabAddNoteButton.extend()
                }
            }
        })
    }


    private fun setUpViewModelAndObserver() {
        //instantiate viewmodel and set up observer
        mViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        mViewModel.readAllNotes.observe(viewLifecycleOwner, { note ->
            adapter.setData(note)
            hideOrShowSearchViewAndPlaceholder()
        })
    }

    private fun hideOrShowSearchViewAndPlaceholder() {
        if (adapter.itemCount < 1) {
            binding.searchView.visibility = View.GONE
            binding.notePlaceholder.visibility = View.VISIBLE
        } else {
            binding.searchView.visibility = View.VISIBLE
            binding.notePlaceholder.visibility = View.INVISIBLE
        }
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
                hideOrShowSearchViewAndPlaceholder()
                Snackbar.make(binding.root, "Successfully deleted all notes", Snackbar.LENGTH_LONG)
                    .show()

            }
            setNegativeButton("No") { _, _ ->

            }

            setTitle("Delete all notes")
            setMessage("Are you sure you want to delete all notes? Notes will be moved to trash")
            setIcon(R.drawable.ic_trash_can)
            create()
            show()
        }
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

    //pop up menu for long press of notes
    override fun popUpMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.inflate(R.menu.pop_up_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addToFavorites_popUpMenu -> {
                    mViewModel.addSingleNoteToFavorites(adapter.returnItemId(position))
                    Snackbar.make(binding.root, "Added to favorites", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.sendToTrash_popUpMenu -> {
                    mViewModel.sendSingleNoteToTrash(
                        System.currentTimeMillis(),
                        adapter.returnItemId(position)
                    )
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
        popupMenu.show()

        val popUp = PopupMenu::class.java.getDeclaredField("mPopup").apply {
            this.isAccessible = true
        }
        val menu = popUp.get(popupMenu)
        menu.javaClass.apply {
            getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

}