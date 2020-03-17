package com.example.myandroidapp.Model

import java.io.Serializable

class Song  : Serializable {
    var id: Int
    var title: String
    var artist: String
    var lyrics: String
    var tags: String


    constructor(id:Int, title:String, artist:String, lyrics:String, tags:String){
        this.id = id
        this.title=title
        this.artist=artist
        this.lyrics=lyrics
        this.tags = tags

    }

}

