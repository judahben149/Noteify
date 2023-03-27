package com.judahben149.note.note.fragment.noteList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.judahben149.note.util.LongPressed
import com.judahben149.note.R
import com.judahben149.note.note.adapter.NoteListAdapter
import com.judahben149.note.databinding.FragmentNoteListBinding
import com.judahben149.note.note.model.Note
import com.judahben149.note.note.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NoteListFragment"

@AndroidEntryPoint
class NoteListFragment : Fragment(), LongPressed {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    val mViewModel: NoteViewModel by activityViewModels()
    private lateinit var adapter: NoteListAdapter


    //initialize animations
    private val rotateOpenAnimation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_rotate_open_animation) }
    private val rotateCloseAnimation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_rotate_close_animation) }
    private val fromBottomAnimation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_from_bottom_animation) }
    private val toBottomAnimation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_to_bottom_animation) }

    private var isComposeButtonClicked = false

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
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        val onNoteItemClicked: (noteItem: Note) -> Unit = { noteItem ->
            val action =
                NoteListFragmentDirections.actionNoteListFragmentToNoteDetailsFragment(
                    noteItem
                )
            Navigation.findNavController(binding.root).navigate(action)
        }
        adapter = NoteListAdapter(requireContext(), this, onNoteItemClicked)

        val rvList = binding.rvList
        rvList.adapter = adapter

        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            rvList.layoutManager = this
        }

        setupObservers()
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

//        setUpFloatingActionButton()

//        binding.fabAddNoteButton.setOnClickListener {
////            Navigation.findNavController(binding.root)
////                .navigate(R.id.action_noteListFragment_to_addNoteFragment)
//            onComposeButtonClicked()
//        }

        binding.fabCreateNote.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        binding.fabCreateTodo.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_noteListFragment_to_createTodoFragment)
        }
    }

    private fun onComposeButtonClicked() {
        setVisibility(isComposeButtonClicked)
        setAnimation(isComposeButtonClicked)
        setButtonClickable(isComposeButtonClicked)

        if (!isComposeButtonClicked) {
            isComposeButtonClicked = true
        } else {
            isComposeButtonClicked = false
        }
    }

    private fun setButtonClickable(isButtonClicked: Boolean) {
        if (!isButtonClicked) {
            binding.fabCreateNote.isClickable = true
            binding.fabCreateTodo.isClickable = true
        } else {
            binding.fabCreateNote.isClickable = false
            binding.fabCreateTodo.isClickable = false
        }
    }

    private fun setVisibility(isButtonClicked: Boolean) {
        if (!isButtonClicked) {
            binding.fabCreateTodo.visibility = View.VISIBLE
            binding.fabCreateNote.visibility = View.VISIBLE
        } else {
            binding.fabCreateTodo.visibility = View.INVISIBLE
            binding.fabCreateNote.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(isButtonClicked: Boolean) {
        if (!isButtonClicked) {
            binding.fabCreateNote.startAnimation(fromBottomAnimation)
            binding.fabCreateTodo.startAnimation(fromBottomAnimation)
//            binding.fabAddNoteButton.startAnimation(rotateOpenAnimation)
        } else {
            binding.fabCreateNote.startAnimation(toBottomAnimation)
            binding.fabCreateTodo.startAnimation(toBottomAnimation)
//            binding.fabAddNoteButton.startAnimation(rotateCloseAnimation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        hideOrShowSearchViewAndPlaceholder()
        Log.d(TAG, "onResume")
    }

//    private fun setUpFloatingActionButton() {
//        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) { // Scrolling down
//                    binding.fabAddNoteButton.shrink()
//                } else { // Scrolling up
//                    binding.fabAddNoteButton.extend()
//                }
//            }
//        })
//    }


    private fun setupObservers() {
        mViewModel.readAllNotes.observe(viewLifecycleOwner) { note ->
            adapter.submitList(note)
            hideOrShowSearchViewAndPlaceholder()
        }
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

        mViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                adapter.submitList(it)
            }

        }
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

        //Use this instead if you want to have menu with icons
//        try {
//            val popUp = PopupMenu::class.java.getDeclaredField("mPopup").apply {
//                this.isAccessible = true
//            }

//            val menu = popUp.get(popupMenu)
//            menu.javaClass.apply {
//                getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                    .invoke(menu, true)
//            }
//        }
//        catch (e: Exception) {
//            Log.e("Popupm", "Error showing menu Icons", e)
//        } finally {
//            popupMenu.show()
//        }
    }

}