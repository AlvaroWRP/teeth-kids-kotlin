package com.example.teethkids

import android.os.Parcel
import android.os.Parcelable

data class EmergencyRequest(
    val id: String,
    val title: String?,
    val description: String?,
    val city: String?,
    val imageUrl1: String?,
    val imageUrl2: String?,
    val imageUrl3: String?,
    val street: String?,
    val streetNumber: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(city)
        parcel.writeString(imageUrl1)
        parcel.writeString(imageUrl2)
        parcel.writeString(imageUrl3)
        parcel.writeString(street)
        parcel.writeString(streetNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmergencyRequest> {
        override fun createFromParcel(parcel: Parcel): EmergencyRequest {
            return EmergencyRequest(parcel)
        }

        override fun newArray(size: Int): Array<EmergencyRequest?> {
            return arrayOfNulls(size)
        }
    }
}
