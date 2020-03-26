package com.example.myandroidapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.myandroidapp.Adapter.ListSongsAdapter
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_search_songs.*
import kotlinx.android.synthetic.main.list_elem_search_songs.*
import java.lang.Math.abs
import kotlin.math.absoluteValue
import kotlin.random.Random


class SearchSongsActivity : AppCompatActivity() {

    internal lateinit var db: DatabaseHelper
    var adapter : ListSongsAdapter? = null
    var s_keyword : String? = null
    var selected_Set = HashSet<Int>()
    var actionMode : ActionMode? = null

//    implementation of Songs Action mode - later implement it as class and interface.
    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.actions_lib_song_menu, menu)
            mode.title = "Elige una opcion"
            return true
        }
        // Called each time the action mode is shown. Always called after onCreateActionMode.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_addToList -> {
                    Toast.makeText(this@SearchSongsActivity, "Add to list", Toast.LENGTH_SHORT).show()
                    val summaryLists = db.getSummaryLists()
                    var listNamesA = ArrayList<String>()
                    for(list in summaryLists) {
                        listNamesA.add(list.name)
                    }
                    if(!listNamesA.contains("Nueva Lista")) {
                        listNamesA.add("Nueva Lista")
                    }
                    else {
                        listNamesA.add("Nueva Lista-"+ abs(Random.nextInt()))
                    }
                    val listNames = listNamesA.toArray(emptyArray<String>())

                    var selected_ListName = ""
                    val copy_Selected_Set = HashSet<Int>(selected_Set)
                    val builder = AlertDialog.Builder(this@SearchSongsActivity)
                    builder.setTitle(R.string.choose_list_to_add_song)
                    builder.setIcon(R.drawable.add_list_icon)
                    builder.setSingleChoiceItems(listNames, -1)  { dialogInterface, i ->
                        selected_ListName = listNames[i]

                        var myListSongs = HashMap<Int, Song>()
                        for(songID in copy_Selected_Set) {
                            myListSongs[songID] = db.getSong(songID)!!
                        }
                        val listID = db.searchSongsListByName(selected_ListName)

                        if(listID > 0) {
                            //   verify before updating/adding in Favoritos SongsList
                            val oldSongsList = db.getSongsList(listID)
                            for(songID in copy_Selected_Set){
                                if(!oldSongsList.songs.containsKey(songID)){
                                    oldSongsList.songs[songID] = db.getSong(songID)!!
                                }
                                else{
                                    print("${songID} already exists")
                                    Toast.makeText(this@SearchSongsActivity, "Song ${songID} is in the List",Toast.LENGTH_SHORT).show()
                                }
                            }
                            db.updateSongsList(SongsList(listID, selected_ListName, oldSongsList.songs))
                        }
                        else {
                            db.addSongsList(selected_ListName, myListSongs.values.toList())
                        }
                        refreshAll()
                        dialogInterface.dismiss()
                    }

                    builder.setNeutralButton("Cancel") { dialog, which ->
                        dialog.cancel()
                    }
                    val mDialog = builder.create()
                    mDialog.show()
                    mode.finish()
                    true
                }
                R.id.action_addToFavs -> {
                    Toast.makeText(this@SearchSongsActivity, "Agregado to Favoritos", Toast.LENGTH_SHORT).show()

                    val songsPerList = HashMap<Int, Song>()

                    for (songID in selected_Set) {
                        songsPerList[songID] = db.getSong(songID)!!
                    }
                    val favSongsListID = db.searchSongsListByName("Favoritos")

                    if(favSongsListID > 0) {
                        //   verify before updating/adding in Favoritos SongsList
                        val oldFavsList = db.getSongsList(favSongsListID)
                        for(songID in selected_Set){
                            if(!oldFavsList.songs.containsKey(songID)){
                                oldFavsList.songs[songID] = db.getSong(songID)!!
                            }
                            else{
                                print("${songID} already exists")
                            }
                        }
                        db.updateSongsList(SongsList(favSongsListID, "Favoritos", oldFavsList.songs))
                    }
                    else {
                        db.addSongsList("Favoritos", songsPerList.values.toList())
                    }
                    refreshAll()
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                R.id.action_deleteSong -> {
                    for(song in selected_Set){
                        //Functionality to check if the song can be deleted (if doesnot belong to a list)
                        try{
                            db.deleteSong(db.getSong(song)!!)
                        }
                        catch(e: IllegalStateException){
                            showImpossibleSongDeletion()
                            break
                        }
                    }
                    adapter?.listSong = db.getSongs()
                    refreshAll()
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else ->
                    false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode){
            actionMode = null
            refreshAll()
        }
    }

    companion object {
        const val EXTRA_KEYWORD = "s_keyword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_songs)

        db = DatabaseHelper(this)
//        searchSpecificSongs()

        val listSong = db.getSongs()
        adapter = ListSongsAdapter(this, listSong, selected_Set)
        songs_lstView.adapter = adapter


        songs_lstView.setOnItemClickListener { parent, view, position, longID ->
            val id = longID.toInt()
            if(selected_Set.isEmpty()) {
                val intent = Intent(this, ViewSongActivity::class.java)
                intent.putExtra(ViewSongActivity.EXTRA_SONG_ID, id)
                startActivity(intent)
            }
            else if(selected_Set.contains(id)){
                selected_Set.remove(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = false
                checkbox.visibility = View.INVISIBLE

                if(selected_Set.isEmpty()) {
                    actionMode?.finish()
                }
            }
            else if(!selected_Set.contains(id)){
                selected_Set.add(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = true
                checkbox.visibility = View.VISIBLE
            }


        }
        songs_lstView.setOnItemLongClickListener { parent, view, position, longID ->
            val id = longID.toInt()

            if(selected_Set.contains(id)) {
                selected_Set.remove(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = false
                checkbox.visibility = View.INVISIBLE

                if(selected_Set.isEmpty()) {
                    actionMode?.finish()
                }
            }
            else if(!selected_Set.contains(id)) {
                selected_Set.add(id)
                val checkbox = view.findViewById<CheckBox>(songCheckBox.id)
                checkbox.isChecked = true
                checkbox.visibility = View.VISIBLE
            }

            //agregar menu de songs
            if(!selected_Set.isEmpty()) {
                when (actionMode) {
                    null -> {
                        // Start the CAB using the ActionMode.Callback defined above
                        actionMode = this@SearchSongsActivity.startActionMode(actionModeCallback)
                    }
                }

            }
            true
        }

    }

    // SEARCH_BAR IMPLEMENTATION
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_bar)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.queryHint = "Buscar..."
        searchView.maxWidth = Int.MAX_VALUE

        searchItem.setOnMenuItemClickListener {
            true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@SearchSongsActivity, "Buscando $query", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val listSongs1 = db.searchSongs(query!!)
                val adapter = ListSongsAdapter(this@SearchSongsActivity, listSongs1, selected_Set)
                songs_lstView.adapter = adapter
                return false
            }
        })

        val keyword_extra: String? = this.intent.getStringExtra(EXTRA_KEYWORD)

        if (!keyword_extra.isNullOrEmpty()) {
//            searchItem.expandActionView()
//            searchView.setQuery(keyword_extra, true)
            val listSongs1 = db.searchSongsByTags(keyword_extra)
            val adapter = ListSongsAdapter(this@SearchSongsActivity, listSongs1, selected_Set)
            songs_lstView.adapter = adapter

        }
        return true
    }

    fun refreshAll() {
        selected_Set.clear()
        adapter?.notifyDataSetChanged()
    }

    fun showImpossibleSongDeletion() {
        val layout =  layoutInflater.inflate(R.layout.toast_unable_delete, findViewById(R.id.toast_layout_root))

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}


