package com.judahben149.note.note.fragment.addNoteToFavorite

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
import com.judahben149.note.databinding.FragmentFavoriteNoteListBinding
import com.judahben149.note.note.adapter.FavoriteNoteListAdapter
import com.judahben149.note.note.viewmodel.FavoriteNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteNoteListFragment  : Fragment() {

    private var _binding: FragmentFavoriteNoteListBinding? = null
    private val binding get() = _binding!!
    private val adapter = FavoriteNoteListAdapter()
    val mViewModel: FavoriteNoteViewModel by viewModels()
    private lateinit var rvList: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvList = binding.rvFavoritesNotesList
        rvList.adapter = adapter

        setupRecyclerViewLayout()
        setUpObservers()
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpObservers() {
        mViewModel.readAllFavoriteNotes().observe(viewLifecycleOwner) { note ->
            adapter.setData(note)
            hideOrShowPlaceholder()
        }
    }

    private fun setupRecyclerViewLayout() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false).apply {
            rvList.layoutManager = this
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_notes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_remove_all_from_favorites -> {
                mViewModel.removeAllNotesFromFavorites()
                Snackbar.make(binding.root, "Notes removed from favorites", Snackbar.LENGTH_SHORT).show()
            }
            R.id.menu_delete_all_favorites -> {
                mViewModel.sendAllFavoriteNotesToTrash(System.currentTimeMillis())
                Snackbar.make(binding.root, "Notes sent to trash", Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideOrShowPlaceholder() {
        if (adapter.itemCount < 1) {
            binding.favoritePlaceholder.visibility = View.VISIBLE
        } else {
            binding.favoritePlaceholder.visibility = View.INVISIBLE
        }
    }

}