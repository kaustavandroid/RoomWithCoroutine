/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kgandroid.roomwithcoroutine.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_YEAR

@Entity(tableName = "word_table")
data class Word(
    val word: String?,
    var meaning: String?,
    var insertDate: String?
    //= Calendar.getInstance()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(wordId)
            parcel.writeString(word)
            parcel.writeString(meaning)
            parcel.writeString(insertDate)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Word> {
            override fun createFromParcel(parcel: Parcel): Word {
                return Word(parcel)
            }

            override fun newArray(size: Int): Array<Word?> {
                return arrayOfNulls(size)
            }
        }
    @PrimaryKey(autoGenerate = true)
    var wordId: Int = 0

    //override fun toString() = word
    }




    //val insertDateString: String = dateFormat.format(insertDate.time)
    /*companion object {
        private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }*/

