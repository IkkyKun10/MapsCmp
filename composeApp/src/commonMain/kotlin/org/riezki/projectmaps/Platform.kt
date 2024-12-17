package org.riezki.projectmaps

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform