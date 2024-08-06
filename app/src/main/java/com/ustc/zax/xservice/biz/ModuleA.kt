package com.ustc.zax.xservice.biz

import android.util.Log
import com.ustc.zax.xservice.UserService
import com.ustc.zax.xservice.service.XOwner

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc: the module who provides the real [UserService]
 */
class ModuleA(owner: XOwner): UserService {

  init {
    owner.active(UserService::class.java, this)
  }

  override fun getUserName(): String {
    // real name
    return "orange"
  }

  override fun logInfo(info: String) {
    Log.d("UserService", "real log $info")
  }

  override fun delayLog(info: String) {
    Log.d("UserService", "real delayLog $info")
  }
}