package com.karabok.workstep.Const

object ConstAPI {
    public val domainLink: String = "https://work-step.ru"

    public val emailSend: String = "/api/v1/EmailCode"
    public val allOrders: String = "/api/v1/select/order/"
    public val insertProfile: String = "/api/v1/insert/profile/"
    public val deleteProfileId: String = "/api/v1/delete/profileId/"
    public val insertUser: String = "/api/v1/insert/user/"
    public val insertOrder: String = "/api/v1/insert/order/"
    public val selectProfileUserId: String = "/api/v1/select/userIdProfile/"

    public val selectEmailUser: String = "/api/v1/select/emailUser/"
    public val selectOrderUserId: String = "/api/v1/select/orderUserId/"
    public val deleteOrderId: String = "/api/v1/delete/orderId/"

    public val updatePasswordUser: String = "/api/v1/update/user/password/"
    public val selectOrdersFilter: String = "/api/v1/select/ordersFilter/"

    public val selectNicknameUser: String = "/api/v1/select/nicknameUser/"
    public val updateNicknameUser: String = "/api/v1/update/user/nickname/"

    public val existUserNickname: String = "/api/v1/exist/userNickname/"
    public val existUserEmail: String = "/api/v1/exist/userEmail/"
    public val selectFriends: String = "/api/v1/select/allfriends/"

    public val webSocket: String = "/ws/chat/"
}