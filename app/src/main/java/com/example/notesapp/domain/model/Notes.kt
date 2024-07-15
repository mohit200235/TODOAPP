package com.example.notesapp.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Notes : RealmObject {

    var _id: Int = 0
    var todo: String = ""
    var completed: Boolean = false
    var _userId: Int = 0

    override fun toString(): String {
        return "Notes(todo='$todo',completed='$completed',_userId='$_userId')"
    }

}