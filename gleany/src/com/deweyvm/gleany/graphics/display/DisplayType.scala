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

package com.deweyvm.gleany.graphics.display

import com.badlogic.gdx.Gdx
import com.deweyvm.gleany.data.Point2i

object DisplayType:
  def fromInt(int: Int): DisplayType = Display.All(int) // fixme error handling

abstract class DisplayType(id: Int):
  def toInt: Int = id

object Display:
  val All: IndexedSeq[DisplayType] =
    Vector(Windowed, Fullscreen, WindowedFullscreen, BorderlessWindow)

  case object Windowed extends DisplayType(0)
  case object Fullscreen extends DisplayType(1)
  case object WindowedFullscreen extends DisplayType(2)
  case object BorderlessWindow extends DisplayType(3)

  def setDisplayMode(mode: DisplayType, defaultWindowSize: Point2i): Unit =
    mode match
      case Fullscreen =>
        // val desktopMode = Gdx.graphics.getDesktopDisplayMode
        // Gdx.graphics.setDisplayMode(desktopMode.width, desktopMode.height, true)
        val desktopMode = Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor)
        Gdx.graphics.setFullscreenMode(desktopMode) // port TODO
      case Windowed =>
        Gdx.graphics.setResizable(true)
        Gdx.graphics.setUndecorated(true)
        Gdx.graphics.setWindowedMode(
          defaultWindowSize.x,
          defaultWindowSize.y
        )
      case WindowedFullscreen =>
        val desktopMode = Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor)
        Gdx.graphics.setResizable(false)
        Gdx.graphics.setUndecorated(true)
        Gdx.graphics.setWindowedMode(
          desktopMode.width,
          desktopMode.height
        )
      case BorderlessWindow =>
        Gdx.graphics.setUndecorated(true)
        Gdx.graphics.setResizable(true)
        Gdx.graphics.setWindowedMode(
          defaultWindowSize.x,
          defaultWindowSize.y
        )

  def resize(mode: DisplayType, dx: Int, dy: Int): Unit =
    val (x, y) = (Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    setDisplayMode(mode, Point2i(x, y))
