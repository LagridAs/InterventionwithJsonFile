package com.example.interventionapp

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
data class Intervention(
    val numero: String?,
    var date: String?,
    var plombier: Plombier?,
    var Type: TypeIv?
                         ):Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("plombier"),
        TODO("Type")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(numero)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Intervention> {
        override fun createFromParcel(parcel: Parcel): Intervention {
            return Intervention(parcel)
        }

        override fun newArray(size: Int): Array<Intervention?> {
            return arrayOfNulls(size)
        }
    }
}