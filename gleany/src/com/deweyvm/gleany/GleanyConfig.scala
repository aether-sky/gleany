/** ****************************************************************************
  * Copyright 2013, deweyvm
  *
  * This file is part of Gleany.
  *
  * Gleany is free software: you can redistribute it and/or modify it under
  * the terms of the GNU General Public License as published by the Free
  * Software Foundation, either version 3 of the License, or (at your option)
  * any later version.
  *
  * Gleany is distributed in the hope that it will be useful, but WITHOUT ANY
  * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
  * details.
  *
  * You should have received a copy of the GNU General Public License along
  * with Gleany.
  *
  * If not, see <http://www.gnu.org/licenses/>.
  * ****************************************************************************/

package com.deweyvm.gleany

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.deweyvm.gleany.graphics.display.Display
import com.deweyvm.gleany.saving.{AudioSettings, VideoSettings}
import com.badlogic.gdx.Files
import com.badlogic.gdx.Graphics.DisplayMode
import com.deweyvm.gleany.data.Point2i


case class GleanyConfig(
    settings: VideoSettings with AudioSettings,
    title: String = "GleanyGame",
    iconPath: Seq[String] = Seq()) {
  def toLwjgl: Lwjgl3ApplicationConfiguration  = lwjglConfig

  def getScreenSize:Point2i = {
    try {
      val desktopSize = java.awt.Toolkit.getDefaultToolkit.getScreenSize
      Point2i(desktopSize.width, desktopSize.height)
    } catch {
      case head:java.awt.HeadlessException =>
        Point2i(1920,1080)
    }
  }


  private val lwjglConfig = {
    val config = new Lwjgl3ApplicationConfiguration()
    config.setTitle( title)
    config.useVsync(true)
    iconPath foreach {
      path =>
        config.setWindowIcon(Files.FileType.Internal, path)
    }
    val desktopSize = getScreenSize
    settings.getDisplayType match {
      case Display.Fullscreen =>
        //val displayModes = Gdx.graphics.getDisplayModes
        ???//port TODO
        //new DisplayMode(desktopSize.x, desktopSize.y, 60, 32)
        //config.setFullscreenMode(displayMode)
      case Display.WindowedFullscreen =>
        config.setWindowedMode(desktopSize.x,desktopSize.y)
        config.setResizable(false)
        config.setDecorated(false);
      case Display.Windowed =>
        val size = settings.getWindowSize
        config.setWindowedMode(size.x,size.y)
      case Display.BorderlessWindow =>
        val size = settings.getWindowSize
        config.setWindowedMode(size.x,size.y)
        config.setResizable(false)
        config.setDecorated(false)
    }
    config
  }

}
