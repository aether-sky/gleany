package com.badlogic.gdx.controllers.desktop

import com.badlogic.gdx.controllers.Controller
import com.deweyvm.gleany.Debug
import com.deweyvm.gleany.input.ControlType

object HackGetPovs {
  def getPovCount(controller: Controller): Int = {
    try {
      val method = controller.getClass.getMethod("getControlCount", classOf[ControlType])
      val numPovs = method.invoke(controller, ControlType.pov).asInstanceOf[Int]
      numPovs
    } catch {
      case nsm:Exception =>
        Debug.debug(nsm.getMessage)
        0
    }
  }
}
