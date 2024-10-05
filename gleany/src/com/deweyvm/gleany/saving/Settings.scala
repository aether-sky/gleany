/** ****************************************************************************
  * Copyright 2013, deweyvm
  *
  * This file is part of Gleany.
  *
  * Gleany is free software: you can redistribute it and/or modify it under the
  * terms of the GNU General Public License as published by the Free Software
  * Foundation, either version 3 of the License, or (at your option) any later
  * version.
  *
  * Gleany is distributed in the hope that it will be useful, but WITHOUT ANY
  * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
  * details.
  *
  * You should have received a copy of the GNU General Public License along with
  * Gleany.
  *
  * If not, see <http://www.gnu.org/licenses/>.
  * ***************************************************************************
  */

package com.deweyvm.gleany.saving

import com.deweyvm.gleany.Debug
import com.deweyvm.gleany.input.JoypadButton
import com.deweyvm.gleany.data.Point2i
import com.deweyvm.gleany.graphics.display.DisplayType
import scala.collection.mutable.ArrayBuffer

class Settings(
    controls: ControlNameCollection[ControlName],
    defaults: SettingDefaults,
    persist: Boolean
) extends VideoSettings
    with AudioSettings:
  import SettingUtils.scalaMapToJava

  Debug.load()
  private val controlListeners = ArrayBuffer[() => Unit]()
  // fixme -- private these?
  val utils = new SettingUtils(controls, defaults)
  val filename = "settings.json"
  val raw: RawSettings =
    if persist then
      LoadUtils.load(
        classOf[RawSettings],
        filename,
        () => utils.makeNew,
        utils.verify
      )
    else utils.makeNew

  def addListener(listener: () => Unit): Unit =
    controlListeners += listener

  override def getDisplayType: DisplayType =
    DisplayType.fromInt(raw.displayType)
  override def setDisplayType(`type`: DisplayType): Unit =
    raw.displayType = `type`.toInt
    flush()

  override def getWindowSize: Point2i =
    Point2i(raw.width.toInt, raw.height.toInt)
  override def setWindowSize(width: Int, height: Int): Unit =
    raw.width = width
    raw.height = height
    flush()

  override def getSfxVolume: Float = raw.sfxVolume
  override def setSfxVolume(value: Float): Unit =
    raw.sfxVolume = value
    flush()

  override def getMusicVolume: Float = raw.musicVolume
  override def setMusicVolume(value: Float): Unit =
    raw.musicVolume = value
    flush()

  def getKey(control: ControlName): Int =
    raw.keyboardControls.get(control.name).toInt
  def setKey(control: ControlName, key: Int): Unit =
    raw.keyboardControls.put(control.name, key.toFloat)
    flush()

  def getJoyButton(control: ControlName): JoypadButton =
    JoypadButton.fromString(raw.joypadControls.get(control.name))
  // fixme error handling
  def setJoyButton(control: ControlName, button: JoypadButton): Unit =
    raw.joypadControls.put(control.name, button.toString)
    flush()

  def resetControls(): Unit =
    raw.joypadControls = scalaMapToJava(controls.makeJoypadDefault)
    raw.keyboardControls = scalaMapToJava(controls.makeKeyboardDefault)
    controlListeners foreach {
      _()
    }
    flush()

  def flush(): Unit =
    // fixme -- flush should verify first
    if persist then 
      LoadUtils.flush(raw, filename)
