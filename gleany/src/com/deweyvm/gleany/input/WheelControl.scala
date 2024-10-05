package com.deweyvm.gleany.input

import com.deweyvm.gleany.GleanyMath

/** Requires that update is called after all querying in a frame
  */
class WheelControl extends Control[Int]:
  def update(): Unit =
    MouseHelper.wrapper.delta = 0
  def isPressed = justPressed
  def justPressed = 
    if MouseHelper.wrapper.delta != 0f then
      Math.signum(MouseHelper.wrapper.delta).toInt
    else
      0
  def justReleased = 0
  def zip(start: Int, num: Int) = 0
