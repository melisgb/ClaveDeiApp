package com.example.myandroidapp.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.list_elem_read_lists.view.*


class ListsOfSongsListAdapter(internal var activity: Activity,
                              var listOfSongsList: List<SongsList>): BaseAdapter(){

    internal var inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val songListView: View = inflater.inflate(R.layout.list_elem_read_lists, null)

        songListView.listNameTxtView.text = listOfSongsList[position].name
//        songListView.listQtySongsTxtView.text = listOfSongsList[position].songs.size

        return songListView

    }

    override fun getItem(position: Int): Any {
        return listOfSongsList[position]
    }

    override fun getItemId(position: Int): Long {
        return listOfSongsList[position].id.toLong()

    }

    override fun getCount(): Int {
        return listOfSongsList.size
    }
}

