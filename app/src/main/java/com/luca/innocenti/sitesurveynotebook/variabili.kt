package com.luca.innocenti.sitesurveynotebook

class variabili {
    private var audio: Boolean = false
    private var photo: Boolean = false

    companion object Factory {
        fun create(): variabili = variabili()
    }

    fun set_audio(valore:Boolean)
    {
        audio = valore
    }

    fun set_foto(valore:Boolean)
    {
        photo = valore
    }

    fun get_audio():Boolean
    {
        return audio
    }

    fun get_photo():Boolean
    {
        return photo
    }

}