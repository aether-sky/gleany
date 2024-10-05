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

import java.util
import com.deweyvm.gleany.data.Point2i
import com.deweyvm.gleany.GleanyMath

object SettingUtils:
  def scalaMapToJava[K, V](map: Map[K, V]): java.util.Map[K, V] =
    val result = new util.HashMap[K, V]()
    for k <- map.keys do result.put(k, map(k))
    result

class SettingUtils(
    controls: ControlNameCollection[ControlName],
    defaults: SettingDefaults
):
  import SettingUtils.scalaMapToJava

  def makeNew: RawSettings =
    RawSettings.makeNew(
      scalaMapToJava(controls.makeKeyboardDefault),
      scalaMapToJava(controls.makeJoypadDefault),
      defaults.WindowSize.x,
      defaults.WindowSize.y,
      defaults.DisplayMode,
      defaults.MusicVolume,
      defaults.SfxVolume
    )

  def verify(raw: RawSettings): RawSettings =
    verify(raw, defaults.WindowSize)

  private def verifyMap[T](
      raw: RawSettings,
      map: java.util.Map[String, T],
      makeDefault: () => java.util.Map[String, T]
  ): java.util.Map[String, T] =
    if map == null then return makeDefault()
    // fixme -- dont need to use java types here anymore
    val foundNames: util.Collection[T] = new util.ArrayList[T]
    for name <- controls.values do
      val button: T = map.get(name.toString)
      if button == null || foundNames.contains(button) then return makeDefault()
      foundNames.add(button)
    map

  def verify(raw: RawSettings, windowSize: Point2i): RawSettings =
    if raw.width < windowSize.x then raw.width = windowSize.x
    if raw.height < windowSize.y then raw.height = windowSize.y
    raw.sfxVolume = GleanyMath.clamp(raw.sfxVolume, 0, 1)
    raw.musicVolume = GleanyMath.clamp(raw.musicVolume, 0, 1)
    if raw.displayType < 0 || raw.displayType > 3 then
      raw.width = windowSize.x
      raw.height = windowSize.y
      raw.displayType = 0
    raw.keyboardControls = verifyMap(
      raw,
      raw.keyboardControls,
      () => scalaMapToJava(controls.makeKeyboardDefault)
    )
    raw.joypadControls = verifyMap(
      raw,
      raw.joypadControls,
      () => scalaMapToJava(controls.makeJoypadDefault)
    )
    raw
