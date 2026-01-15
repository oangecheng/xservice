package com.ustc.zax.xservice.service

import androidx.lifecycle.Observer
import java.lang.reflect.Proxy

/**
 * Time: 2024/8/6
 * Simple is better than complex ~
 * Desc:
 */
@Suppress("UNCHECKED_CAST")
class XManager : XProxy, XOwner {

  private val xHolders = HashMap<Class<*>, XHolder<*>>()
  private val services = HashMap<Class<*>, XService>()

  fun <T : XService> register(cls : Class<T>, def : T) {
    val xHolder = XHolder.instance(def)
    services[cls] = proxy(cls, xHolder)
    xHolders[cls] = xHolder
  }

  override fun <T : XService> get(cls: Class<T>): T {
    val service = services[cls]
    return if (service != null) {
      service as T
    } else {
      throw IllegalAccessError(
        "$cls not registered"
      )
    }
  }

  override fun <T : XService> isActivated(cls: Class<T>): Boolean {
    return xHolders[cls]?.get() != null
  }

  override fun <T : XService> monitor(cls: Class<T>, ob: Observer<T>) {
    xHolders[cls]?.let {
      (it as XHolder<T>).monitor(ob)
    }
  }

  override fun <T : XService> active(cls: Class<T>, s: T) {
    xHolders[cls]?.let {
      (it as XHolder<T>).activate(s)
    }
  }


  private fun <T : XService> proxy(
    clz: Class<T>,
    holder: XHolder<T>
  ): T {
    return Proxy.newProxyInstance(
      clz.classLoader,
      arrayOf(clz),
      XInvoker(holder)
    ) as T
  }
}