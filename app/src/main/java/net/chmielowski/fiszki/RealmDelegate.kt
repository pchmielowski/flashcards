package net.chmielowski.fiszki

import io.realm.Realm

internal class RealmDelegate {
    lateinit var realm: Realm

    fun onCreate() {
        realm = Realm.getDefaultInstance()
    }

    fun onDestroy() {
        realm.close()
    }
}
