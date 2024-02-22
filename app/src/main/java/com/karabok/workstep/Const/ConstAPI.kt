package com.karabok.workstep.Const

object ConstAPI {
    public val domainLink: String = "https://work-step.ru"

    public val emailSend: String = "/api/v1/EmailCode"
    public val allOrders: String = "/api/v1/select/order/"
    public val insertProfile: String = "/api/v1/insert/profile/"
    public val deleteProfileId: String = "/api/v1/delete/profileId/"
    public val insertUser: String = "/api/v1/insert/user/"
    public val selectProfileUserId: String = "/api/v1/select/userIdProfile/"

    public val existUserNickname: String = "/api/v1/exist/userNickname/"
    public val existUserEmail: String = "/api/v1/exist/userEmail/"
    public val selectFriends: String = "/api/v1/select/allfriends/"

    public val webSocket: String = "/ws/chat/"
}