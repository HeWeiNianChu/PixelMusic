package com.kyant.pixelmusic.api

import com.beust.klaxon.Klaxon
import com.kyant.pixelmusic.api.lyrics.LyricResult
import com.kyant.pixelmusic.util.toLyrics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

typealias Time = String
typealias Content = String
typealias Lyrics = Map<Time, Content>

val EmptyLyrics: Lyrics = emptyMap()

suspend fun SongId.findLyrics(): Lyrics? = withContext(Dispatchers.IO) {
    Klaxon().parse<LyricResult>(URL("$API/lyric?id=${this@findLyrics}").readText())?.lrc?.lyric?.toLyrics()
}