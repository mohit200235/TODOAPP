package com.example.notesapp

import android.app.Application
import com.example.notesapp.domain.model.Notes
import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.migration.RealmMigration

class Application : Application() {

    companion object{
        lateinit var realm:Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.Builder(
                schema = setOf(Notes::class)
            )
                .schemaVersion(2)
                .build()
        )
    }

}