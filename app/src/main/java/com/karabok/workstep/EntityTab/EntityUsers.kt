package com.karabok.workstep.EntityTab

import android.os.Parcel
import android.os.Parcelable

data class EntityUsers(
    val id: Int,
    val email: String?,
    val password: String?,
    val nickname: String?,
    val dateReg: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(nickname)
        parcel.writeString(dateReg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityOrders> {
        override fun createFromParcel(parcel: Parcel): EntityOrders {
            return EntityOrders(parcel)
        }

        override fun newArray(size: Int): Array<EntityOrders?> {
            return arrayOfNulls(size)
        }
    }
}
