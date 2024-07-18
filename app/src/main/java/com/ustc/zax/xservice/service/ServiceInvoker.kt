package com.ustc.zax.xservice.service

import androidx.lifecycle.Observer
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Time: 2024/7/17
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
interface ServiceInvoker<T> : InvocationHandler {

  companion object {
    fun <T : XService<T>> instance(default: T) : ServiceInvoker<T> {
      return ServiceInvokerImpl(default)
    }
  }
}

private class ServiceInvokerImpl<T : XService<T>>(
  private val default: XService<T>
) : ServiceInvoker<T> {

  private var delegate: T? = null
  private val actions = ArrayList<Runnable>()
  private val observers = ArrayList<Observer<T>>()

  @Suppress("UNCHECKED_CAST")
  override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
    // hook基类的 setDelegate 方法
    if (method.name == "setDelegate") {
      val arg = args?.get(0)
      if (arg is XService<*>) {
        delegate = arg as T
        observers.forEach {
          it.onChanged(arg)
        }
        actions.forEach {
          it.run()
        }
        actions.clear()
        return null
      } else {
        throw IllegalArgumentException(
          "invalid service delegate"
        )
      }
    }

    if (method.name == "isAvailable") {
      return delegate != null
    }

    // hook基类的observe，如果已经有服务了，直接回调
    if (method.name == "observe") {
      val arg = args?.get(0)
      if (arg != null) {
        delegate?.let {
          (arg as Observer<T>).onChanged(it)
        } ?: run {
          observers.add(arg as Observer<T>)
        }
      } else {
        throw IllegalArgumentException(
          "invalid observer"
        )
      }
      return Unit
    }

    // 如果真实服务有值，全部走真实服务
    val real = delegate
    if (real != null) {
      return if (args != null) {
        method.invoke(real, *args)
      } else {
        method.invoke(real)
      }
    }

    // 根据注解判断是否需要等待真实服务加载进入之后再执行
    // 动作会被缓存成为一个任务，等delegate被设置值后，就会执行所有被缓存的任务
    if (method.isAnnotationPresent(ServiceTask::class.java)) {
      actions.add(Runnable {
        if (args != null) {
          method.invoke(delegate, *args)
        } else {
          method.invoke(delegate)
        }
      })
    }

    return if (args != null) {
      method.invoke(default, *args)
    } else {
      method.invoke(default)
    }
  }
}