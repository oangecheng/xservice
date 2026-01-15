package com.ustc.zax.xservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ustc.zax.xservice.biz.ModuleA
import com.ustc.zax.xservice.biz.ModuleB
import com.ustc.zax.xservice.service.XManager

/**
 * Time: 2024/7/18
 * Beautiful is better than ugly ~
 * Desc:
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val manager = XManager()
    manager.register(UserService::class.java, object : UserService {})

    // ModuleB init before ModuleA
    // use UserService
    val mb = ModuleB(manager)
    // test immediately
    mb.test()

    // init moduleA and provide UserService
    val ma = ModuleA(manager)
    // test again
    mb.test()
  }
}