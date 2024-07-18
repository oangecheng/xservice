package com.ustc.zax.xservice.service

/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Simple is better than complex ~
 * Desc: get the service
 */
interface XServices {
  fun <T : XService<T>> get(cls : Class<T>) : T
}