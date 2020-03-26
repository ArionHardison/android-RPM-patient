package com.queenbee.app.session

interface SessionListener {
    fun invalidate()
    fun refresh()
}