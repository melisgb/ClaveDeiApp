package com.example.myandroidapp.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.list_elem_search_songs.view.*

// This is required for the ListView
class ListSongsAdapter(internal var activity: Activity,
                       internal var listSong: List<Song>):BaseAdapter(){

    internal var inflater:LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val songView:View
        songView = inflater.inflate(R.layout.list_elem_search_songs, null)

        songView.songTitleTxtView.text = listSong[position].title
        songView.songArtistTxtView.text = listSong[position].artist
        songView.songTagsTxtView.text = listSong[position].tags

//        //Event
//        songView.setOnClickListener{
//            songId_editTxt.setText(songView.songIdTxtView.text.toString())
//            songTitle_editTxt.setText(songView.songTitleTxtView.text)
//            songArtist_editTxt.setText(songView.songArtistTxtView.text)
//            songLyrics_editTxt.setText(songView.songLyricsTxtView.text)
//            songTags_editTxt.setText(songView.songTagsTxtView.text)
//        }
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

