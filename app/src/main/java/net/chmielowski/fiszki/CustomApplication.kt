package net.chmielowski.fiszki

import android.app.Application

import io.realm.Realm

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
