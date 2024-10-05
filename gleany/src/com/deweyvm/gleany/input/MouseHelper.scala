package com.deweyvm.gleany.input

import com.badlogic.gdx.{Gdx, InputAdapter, InputProcessor}

object MouseHelper:
  val wrapper = new MouseWrapper()

class MouseWrapper:
  var delta = 0f
  val listener = new InputAdapter {
    override def scrolled(x:Float, y:Float): Boolean =
      delta = y
      false
  }
  Gdx.input.setInputProcessor(listener)
