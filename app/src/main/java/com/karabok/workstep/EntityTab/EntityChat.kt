package com.karabok.workstep.EntityTab

import android.os.Parcel
import android.os.Parcelable

data class EntityChat(
    val userId: Int,
    val nameChat: String?,
    val dateMessage: String?,
    val textMessage: String?,
    val publicKey: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(nameChat)
        parcel.writeString(dateMessage)
        parcel.writeString(textMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityChat> {
        override fun createFromParcel(parcel: Parcel): EntityChat {
            return EntityChat(parcel)
        }

        override fun newArray(size: Int): Array<EntityChat?> {
            return arrayOfNulls(size)
        }
    }
}
