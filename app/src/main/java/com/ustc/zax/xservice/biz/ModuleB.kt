package com.ustc.zax.xservice.biz

import android.util.Log
import com.ustc.zax.xservice.UserService
import com.ustc.zax.xservice.service.XProxy

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc: the module who use [UserService]
 */
class ModuleB(private val proxy: XProxy) {

  fun test() {
    val user = proxy.get(UserService::class.java)
    Log.d("UserService", "call by moduleB ${user.getUserName()}")
    user.logInfo("log by moduleB")
    user.delayLog("delay log by moduleB")
  }
}