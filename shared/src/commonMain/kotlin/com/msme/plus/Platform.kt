package com.msme.plus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform