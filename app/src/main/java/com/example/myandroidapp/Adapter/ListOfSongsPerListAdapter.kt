package com.example.myandroidapp.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.R
import kotlinx.android.synthetic.main.list_elem_view_songs_list.view.*

class ListOfSongsPerListAdapter(internal var activity: Activity,
                                 var listOfSongsPerList: List<Song>): BaseAdapter(){

    internal var inflater:LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val songView: View = inflater.inflate(R.layout.list_elem_view_songs_list, null)

        songView.songTitleOnListTxtView.text = listOfSongsPerList[position].title
//        songView.songCkBoxOnListCheckBox
//        songView.songImgOnListImageView

        return songView
    }

    override fun getItem(position: Int): Any {
        return listOfSongsPerList[position]
    }

    override fun getItemId(position: Int): Long {
        return listOfSongsPerList[position].id.toLong()

    }

    override fun getCount(): Int {
        return listOfSongsPerList.size
    }
}
