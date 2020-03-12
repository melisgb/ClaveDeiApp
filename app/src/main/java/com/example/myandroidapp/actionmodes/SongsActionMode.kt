package com.example.myandroidapp.actionmodes

import android.app.Activity
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.myandroidapp.R

class SongsActionMode  : ActionMode.Callback {
    val activity : Activity

    constructor(activity: Activity){
        this.activity = activity
    }

    // Called when the action mode is created; startActionMode() was called
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        // Inflate a menu resource providing context menu items
        val inflater: MenuInflater = mode.menuInflater
        inflater.inflate(R.menu.actions_song_menu, menu)
        return true
    }
    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_addToFavs -> {
                Toast.makeText(activity, "Add Favs", Toast.LENGTH_SHORT).show()
                mode.finish() // Action picked, so close the CAB
                true
            }
            else ->
                false

        }
    }

    // Called when the user exits the action mode
    override fun onDestroyActionMode(mode: ActionMode){
//        this.mode = null
    }
}