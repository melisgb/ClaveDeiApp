package com.example.myandroidapp.Model

import java.io.Serializable

class Song  : Serializable {
    var id: Int = 0
    var title: String?= null
    var artist: String?= null
    var lyrics: String?= null
    var tags: String?= null


    constructor(id:Int, title:String, artist:String, lyrics:String, tags:String){
        this.id = id
        this.title=title
        this.artist=artist
        this.lyrics=lyrics
        this.tags = tags

    }

}

