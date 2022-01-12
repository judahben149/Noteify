package com.judahben149.note.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.judahben149.note.databinding.NoteItemBinding
import com.judahben149.note.fragments.favoriteNotes.FavoriteNoteListFragmentDirections
import com.judahben149.note.model.Note
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class FavoriteNoteListAdapter(): RecyclerView.Adapter<FavoriteNoteListAdapter.FavoriteNoteListViewHolder>() {

    var favNoteList = emptyList<Note>()

    inner class FavoriteNoteListViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            val currentNote = favNoteList[position]
            val prettyTime: String = PrettyTime().format(Date(currentNote.timeUpdated))

            binding.tvNoteTitle.text = currentNote.noteTitle
            binding.tvNoteDescription.text = currentNote.noteBody
            binding.tvNoteDate.text = prettyTime

            binding.noteItem.setOnClickListener {
                val action = FavoriteNoteListFragmentDirections.actionFavoriteNoteListFragmentToFavoriteNoteDetailsFragment(currentNote)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteNoteListViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteNoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteNoteListViewHolder, position: Int) {
        holder.bindItem(position)
    }

    override fun getItemCount() = favNoteList.size


    fun setData(note: List<Note>) {
        this.favNoteList = note
        notifyDataSetChanged()
    }
}