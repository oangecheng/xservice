package com.ustc.zax.xservice.service

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Time: 2024/8/6
 * Simple is better than complex ~
 * Desc:
 */
internal class XInvoker<T : XService>(
  private val holder: XHolder<T>
) : InvocationHandler {

  private var service: T? = null
  private val pendingTasks = lazy {
    ArrayList<Runnable>()
  }

  init {
    holder.monitor {
      service = it
      // 分发任务，交给真正的Service执行
      if (pendingTasks.isInitialized()) {
        pendingTasks.value.let { tasks ->
          tasks.forEach { r -> r.run() }
          tasks.clear()
        }
      }
    }
  }

  override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {

    // 如果真实服务有值，全部走真实服务
    service?.let {
      return invokeMethod(
        it, method, args
      )
    }

    if (method.isAnnotationPresent(XDelayTask::class.java)) {
      return pendingTask(
        method,
        args
      )
    }

    val def = holder.def()
    return invokeMethod(
      def,
      method,
      args
    )
  }

  private fun pendingTask(method: Method, args: Array<out Any>?): Any {
    if (method.returnType != Void.TYPE) {
      throw IllegalArgumentException(
        "${method.name} return type is invalid, it should be void"
      )
    }
    pendingTasks.value.add(
      Runnable {
        service?.let {
          invokeMethod(it, method, args)
        }
      }
    )
    return Unit
  }


  private fun invokeMethod(
    target: XService,
    method: Method,
    args: Array<out Any>?
  ): Any? {
    return try {
      if (args != null) {
        method.invoke(target, *args)
      } else {
        method.invoke(target)
      }
    } catch (e: Exception) {
      Log.e("TAG", "invoke method error", e)
    }
  }
}