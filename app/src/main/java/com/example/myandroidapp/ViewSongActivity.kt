package com.example.myandroidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList
import com.example.myandroidapp.db.DatabaseHelper
import kotlinx.android.synthetic.main.activity_view_song.*
import androidx.appcompat.app.AlertDialog

class ViewSongActivity : AppCompatActivity() {

    internal lateinit var db:DatabaseHelper
    var song_id : Int? = null
    var actionMode : ActionMode? = null
    companion object {
        const val EXTRA_SONG_ID = "song_id"
    }


    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.actions_view_song_menu, menu)
            mode.title = "Elige una opcion"
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_addSongToList -> {
                    val summaryLists = db.getSummaryLists()
                    var listNamesA = ArrayList<String>()
                    for(list in summaryLists) {
                        listNamesA.add(list.name)
                    }
//                    if(!listNamesA.contains("Nueva Lista")) {
//                        listNamesA.add("Nueva Lista")
//                    }
//                    else {
//                        listNamesA.add("Nueva Lista"+ Random.nextInt())
//                    }
                    val listNames = listNamesA.toArray(emptyArray<String>())
//
                    var selected_ListName = ""
                    val builder = AlertDialog.Builder(this@ViewSongActivity)
                    builder.setTitle(R.string.choose_list_to_add_song)
                    builder.setIcon(R.drawable.add_list_icon)
                    builder.setSingleChoiceItems(listNames, -1)  { dialogInterface, i ->
                        selected_ListName = listNames[i]
                        var myListSongs = HashMap<Int, Song>()
                        myListSongs[song_id!!] = db.getSong(song_id!!)!!
                        val listID = db.searchSongsListByName(selected_ListName)

                        if(listID > 0) {
                            //   verify before updating/adding in Favoritos SongsList
                            val oldSongsList = db.getSongsList(listID)
                            if(!oldSongsList.songs.containsKey(song_id!!)) {
                                oldSongsList.songs[song_id!!] = db.getSong(song_id!!)!!
                            }
                            else{
                                print("${song_id} already exists")
                            }
                            db.updateSongsList(SongsList(listID, selected_ListName, oldSongsList.songs))
                        }
                        else {
                            val songsPerList = HashMap<Int, Song>()
                            songsPerList[song_id!!] = db.getSong(song_id!!)!!
                            db.addSongsList(selected_ListName, myListSongs.values.toList())
                        }

//                        refreshAll()
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


                R.id.action_addSongToFavs -> {
                    val favSongsListID = db.searchSongsListByName("Favoritos")
                    if(favSongsListID > 0) {
                        //   verify before updating/adding in Favoritos SongsList
                       val oldFavsList = db.getSongsList(favSongsListID)
                       if(!oldFavsList.songs.containsKey(song_id)) {
                                oldFavsList.songs[song_id!!] = db.getSong(song_id!!)!!
                       }
                       else{
                           print("${song_id} already exists")
                       }
                        db.updateSongsList(SongsList(favSongsListID, "Favoritos", oldFavsList.songs))
                    }
                    else {
                        val songsPerList = HashMap<Int, Song>()
                        songsPerList[song_id!!] = db.getSong(song_id!!)!!
                        db.addSongsList("Favoritos", songsPerList.values.toList())
                    }
//                    refreshAll()
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                R.id.action_editSong -> {
                    val intent = Intent(this@ViewSongActivity, CreateSongActivity::class.java)
                    intent.putExtra(CreateSongActivity.EXTRA_SONG_ID, song_id)
                    startActivity(intent)
                    true
                }
                R.id.action_deleteSong -> {
                    try{
                        db.deleteSong(db.getSong(song_id!!)!!)
                    }
                    catch(e: IllegalStateException){
                        showImpossibleSongDeletion()
                    }
                    val intent = Intent(this@ViewSongActivity, SearchSongsActivity::class.java)
                    startActivity(intent)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else ->
                    false
            }
        }
        override fun onDestroyActionMode(mode: ActionMode){
            actionMode = null
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_song)

        db = DatabaseHelper(this)
        val songId_extra = this.intent.getIntExtra(EXTRA_SONG_ID, 0)
        val current_Song: Song? = db.getSong(songId_extra)

        if (songId_extra != 0) {
            song_id = current_Song?.id
            songTitleTxtView.setText(current_Song?.title)
            songArtistTxtView.setText(current_Song?.artist)
            songLyricsTxtView.setText(current_Song?.lyrics)
            songTagsTxtView.setText(current_Song?.tags)
            readSongTitleTxtView.setText(current_Song?.title)

            when (actionMode) {
                null -> {
                    // Start the CAB using the ActionMode.Callback defined above
                    actionMode = this@ViewSongActivity.startActionMode(actionModeCallback)
                }
            }
        }
    }
    fun showImpossibleSongDeletion() {
        val layout =  layoutInflater.inflate(R.layout.toast_unable_delete, findViewById(R.id.toast_layout_root))

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}
