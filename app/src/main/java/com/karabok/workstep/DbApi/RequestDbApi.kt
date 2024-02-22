package com.karabok.workstep.DbApi

import com.karabok.workstep.Const.ConstAPI
import com.karabok.workstep.Utils.Requests

object RequestDbApi {
    public fun select(method: String): String{
        val url = "${ConstAPI.domainLink}${method}"
        return Requests.get(url)
    }

    public fun select(method: String, params: String): String{
        val url = "${ConstAPI.domainLink}$method?$params"
        return Requests.get(url, params)
    }

    public fun delete(method: String, data: String){
        val url = "${ConstAPI.domainLink}${method}"
        Requests.delete(url, data)
    }

    public fun insert(method: String, data: String): String{
        val url = "${ConstAPI.domainLink}${method}"
        return Requests.post(url, data)
    }

    public fun update(method: String, data: String){
        val url = "${ConstAPI.domainLink}${method}"
        Requests.patch(url, data)
    }
}