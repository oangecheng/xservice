package com.ustc.zax.xservice.service

import androidx.annotation.Keep
import androidx.lifecycle.Observer

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc: base Service
 */
@Keep
interface XService<T> {

  fun setDelegate(service: T) {}

  fun observe(observer: Observer<T>) {}

  fun isAvailable(): Boolean {
    return false
  }

}