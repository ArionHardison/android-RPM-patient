package com.queenbee.app.session

object SessionManager {

    private lateinit var listener: SessionListener

    fun instance(listener: SessionListener) {
        SessionManager.listener = listener
    }

    fun clearSession() {
        if(listener!=null)
            listener.invalidate()
    }


}