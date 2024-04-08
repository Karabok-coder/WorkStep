package com.karabok.workstep.EntityTab

import android.os.Parcel
import android.os.Parcelable

data class EntityOrders(
    val id: Int,
    val nameWork: String?,
    val timeStart: Int,
    val timeEnd: Int,
    val description: String?,
    val salary: Int,
    val cityName: String?,
    val timePublish: String?,
    val userAuthor: Int,
    val categoryName: String?,
    val subcategoryName: String?,
    val dateStart: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameWork)
        parcel.writeInt(timeStart)
        parcel.writeInt(timeEnd)
        parcel.writeString(description)
        parcel.writeInt(salary)
        parcel.writeString(cityName)
        parcel.writeString(timePublish)
        parcel.writeInt(userAuthor)
        parcel.writeString(categoryName)
        parcel.writeString(subcategoryName)
        parcel.writeString(dateStart)
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