package com.judahben149.note.fragments.favoriteNotes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judahben149.note.R
import com.judahben149.note.adapters.FavoriteNoteListAdapter
import com.judahben149.note.databinding.FragmentFavoriteNoteListBinding
import com.judahben149.note.viewmodel.FavoriteNoteViewModel

class FavoriteNoteListFragment  : Fragment() {

    private var _binding: FragmentFavoriteNoteListBinding? = null
    private val binding get() = _binding!!
    private val adapter = FavoriteNoteListAdapter()
    private lateinit var mViewModel: FavoriteNoteViewModel
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

        setupRecyclerViewLayout() //this has a function which sets up divider
        setUpViewModel()
        setUpObservers()
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun setUpViewModel() {
        mViewModel = ViewModelProvider(this).get(FavoriteNoteViewModel::class.java)
    }

    private fun setUpObservers() {
        mViewModel.readAllFavoriteNotes.observe(viewLifecycleOwner, { note ->
            adapter.setData(note)
        })
    }

    private fun setupRecyclerViewLayout() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false).apply {
            rvList.layoutManager = this
        }
        recyclerViewDivider(rvList, layoutManager)
    }


    private fun recyclerViewDivider(rvList: RecyclerView, layoutManager: LinearLayoutManager) {
        //this adds the divider line in between each item
        DividerItemDecoration(requireContext(), layoutManager.orientation)
            .apply {
                rvList.addItemDecoration(this)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_notes_menu, menu)
    }

}