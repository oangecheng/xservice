package com.ustc.zax.xservice.service

import java.lang.reflect.Proxy


/**
 * Time: 2024/7/17
 * Author: chengzhi@kuaishou.com
 * Beautiful is better than ugly ~
 * Desc:
 */
class ServiceManager : XServices {

  private val services = HashMap<Class<*>, XService<*>>()

  /**
   * 注册Service
   * @param clz class
   * @param def Service 空实现
   */
  fun <T : XService<T>> register(clz: Class<T>, def: T) {
    services[clz] = proxy(clz, def)
  }

  override fun <T : XService<T>> get(cls: Class<T>): T {
    return getService(cls)
  }

  /**
   * 获取Service
   * @param cls Service的class类型
   * 需要提前注册，否则获取会抛异常
   */
  @Suppress("UNCHECKED_CAST")
  private fun <T : XService<T>> getService(cls: Class<T>): T {
    val service = services[cls]
    return if (service != null) {
      service as T
    } else {
      throw IllegalAccessException(
        "service not found"
      )
    }
  }

  /**
   * auto create a service by [Proxy.newProxyInstance]
   * the service is always no-null when you call [get] function
   * 通过动态代理创建一个Service对象
   * 保证了任何时刻调用[get]函数其返回值都是非空的
   * @param clz class
   * @param def service的默认实现
   */
  @Suppress("UNCHECKED_CAST")
  private fun <T : XService<T>> proxy(
    clz: Class<T>,
    def: T
  ): T {
    return Proxy.newProxyInstance(
      clz.classLoader,
      arrayOf(clz),
      ServiceInvoker.instance(def)
    ) as T
  }
}