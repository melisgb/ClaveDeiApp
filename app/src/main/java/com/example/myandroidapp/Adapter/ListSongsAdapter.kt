package com.example.myandroidapp.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.list_elem_search_songs.view.*

// This is required for the ListView
class ListSongsAdapter(
    internal var activity: Activity,
    var listSong: List<Song>,
    val selectedSet: HashSet<Int>
):BaseAdapter(){

    internal var inflater:LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val songView: View = inflater.inflate(R.layout.list_elem_search_songs, null)

        songView.songTitleTxtView.text = listSong[position].title
        songView.songArtistTxtView.text = listSong[position].artist
        songView.songTagsTxtView.text = listSong[position].tags

        if(selectedSet.contains(listSong[position].id)) {
            songView.songCheckBox.isVisible = true
            songView.songCheckBox.isChecked = true
        }


        return songView

    }

    override fun getItem(position: Int): Any {
        return listSong[position]
    }

    override fun getItemId(position: Int): Long {
        return listSong[position].id.toLong()

    }

    override fun getCount(): Int {
        return listSong.size
    }
}

