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

package com.deweyvm.gleany.fluid

import collection.mutable.ArrayBuffer

class FluidTable(val cols: Int, val rows: Int, maxAdjacent: Int):
  val particles: ArrayBuffer[Drop] = new ArrayBuffer[Drop](rows * cols)
  var grid: Array[ArrayBuffer[Drop]] = resetGrid
  val emptyList: ArrayBuffer[Drop] = ArrayBuffer[Drop]()

  def resetGrid = Array.tabulate(rows * cols) { i =>
    new ArrayBuffer[Drop](maxAdjacent)
  }

  def +=(drop: Drop): Unit =
    particles += drop

  def -=(drop: Drop): Unit =
    particles -= drop

  def length: Int = particles.length

  def clear(): Unit =
    grid = resetGrid

  def foreach(func: Drop => Unit): Unit =
    particles foreach func

  def getAdjacent(drop: Drop): ArrayBuffer[Drop] =
    val x = drop.x.toInt
    val y = drop.y.toInt
    if !inRange(x, y) then return emptyList
    grid(x * rows + y)

  def updateGrid(drop: Drop): Unit =
    val x = drop.x.toInt
    val y = drop.y.toInt
    val xPrev = drop.xPrev.toInt
    val yPrev = drop.xPrev.toInt
    if inRange(xPrev, yPrev) then grid(xPrev * rows + yPrev) -= drop
    if inRange(x, y) then grid(x * rows + y) += drop
    ()

  def rehash(): Unit =
    grid foreach {
      _.clear()
    }
    particles foreach addInterRadius

  def addInterRadius(drop: Drop): Unit =
    val posX = drop.x.toInt
    val posY = drop.y.toInt

    for i <- -1 until 2; j <- -1 until 2 do
      val x = posX + i
      val y = posY + j
      if inRange(x, y) then grid(x * rows + y) += drop
      ()

  def inRange(x: Float, y: Float): Boolean =
    x > 0 && x < cols && y > 0 && y < rows
