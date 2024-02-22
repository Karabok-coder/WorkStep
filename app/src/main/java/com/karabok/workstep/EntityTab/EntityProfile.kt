package com.karabok.workstep.EntityTab

import java.sql.Time

data class EntityProfile(
    val id: Int,
    val firstName: String,
    val description: String,
    val rating: Float,
    val amountWork: Int,
    val userId: Int,
    val timeWorkStart: Time,
    val timeWorkEnd: Time,
    val AcceptAndCancel: Int
)