package com.igtuapps.chaoscomputerstreams.network

data class Conference(
    val conference: String,
    val slug: String,
    val groups: List<Group>
)

data class Group(
    val group: String,
    val rooms: List<Room>
)

data class Room(
    val slug: String,
    val schedulename: String,
    val display: String,
    val streams: List<Stream>
)

data class Stream(
    val slug: String,
    val display: String,
    val type: String,
    val isTranslated: Boolean,
    val videoSize: List<Int>?,
    val urls: Map<String, HlsUrl>
)

data class HlsUrl(
    val display: String,
    val tech: String,
    val url: String
)
