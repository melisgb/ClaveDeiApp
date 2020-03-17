package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myandroidapp.Adapter.ListsOfSongsListAdapter
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_read_lists.*

class ReadListsActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var adapter : ListsOfSongsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_lists)


        db = DatabaseHelper(this)

        val songsLists = db.getSummaryLists()
        adapter = ListsOfSongsListAdapter(this, songsLists)
        myListsListView.adapter = adapter

        myListsListView.setOnItemClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            val intent = Intent(this, ViewListActivity::class.java)
            intent.putExtra(ViewListActivity.EXTRA_LIST_ID, id)
            startActivity(intent)


        }



    }


}


