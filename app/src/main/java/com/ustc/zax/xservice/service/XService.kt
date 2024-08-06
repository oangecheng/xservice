package com.ustc.zax.xservice.service

import androidx.lifecycle.Observer


/**
 * Time: 2024/7/18
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc: base Service
 */
interface XService


interface XHolder<T : XService> {

  companion object {
    fun <T : XService> instance(def: T): XHolder<T> {
      return XHolderImpl(def)
    }
  }

  fun monitor(ob: Observer<T>)
  fun get(): T?
  fun activate(t: T)
  fun def() : T
}

private class XHolderImpl<T : XService>(private val def: T) : XHolder<T> {
  private var service: T? = null

  private val observers by lazy {
    ArrayList<Observer<T>>()
  }

  override fun def(): T {
    return def
  }

  override fun monitor(ob: Observer<T>) {
    observers.add(ob)
    service?.let { ob.onChanged(it) }
  }

  override fun get(): T? {
    return service
  }

  override fun activate(t: T) {
    service = t
    observers.forEach {
      it.onChanged(t)
    }
  }
}


interface XProxy {
  fun <T : XService> get(cls: Class<T>): T
  fun <T : XService> isActivated(cls: Class<T>): Boolean
  fun <T : XService> monitor(cls: Class<T>, ob: Observer<T>)
}


interface XOwner {
  fun <T : XService> active(cls: Class<T>, s: T)
}