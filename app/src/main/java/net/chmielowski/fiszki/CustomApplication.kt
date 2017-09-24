package net.chmielowski.fiszki

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration


class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupRealm()
        setupStetho()
        this.registerActivityLifecycleCallbacks(MyCallbacks(applicationContext))
    }

    private fun setupRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())
    }

    private fun setupStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider
                                .builder(this)
                                .build())
                        .build())
    }
}

class MyCallbacks(val context: Context) : Application.ActivityLifecycleCallbacks {
    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
    }

    private val realmDelegate = RealmDelegate()

    private val lessonService = Game(realmDelegate)

    private val roomService = RoomService(context)

    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        if (activity is MainActivity) {
            activity.realmDelegate = realmDelegate
            activity.game = lessonService
            activity.roomService = roomService
        }
        if (activity is StartActivity) {
            activity.roomService = roomService
        }
    }

    override fun onActivityPaused(p0: Activity?) {
    }

}
