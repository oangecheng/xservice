package com.ustc.zax.xservice

import android.util.Log
import com.ustc.zax.xservice.service.XDelayTask
import com.ustc.zax.xservice.service.XService

/**
 * Time: 2024/8/6
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc:
 */
interface UserService : XService {

  fun getUserName(): String {
    return "default user name"
  }

  fun logInfo(info: String) {
    Log.d("UserService", "default log $info")
  }

  @XDelayTask
  fun delayLog(info: String) {
    Log.d("UserService", "default delayLog $info")
  }
}