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

package com.deweyvm.gleany.data

import com.deweyvm.gleany.GleanyMath

class BoundedPointer(max: Int) extends Pointer(max):
  protected var sign = 1

  override def increment(): Unit =
    raw = GleanyMath.clamp(raw + sign * 1, 0, max - 1)

  override def decrement(): Unit =
    raw = GleanyMath.clamp(raw - sign * 1, 0, max - 1)

  def isAtBeginning: Boolean = raw <= 0

  def isAtEnd: Boolean = raw >= max - 1

  def reverse(): Unit =
    sign = -sign

class LoopedBoundedPointer(max: Int) extends BoundedPointer(max):
  override def increment(): Unit =
    raw = (raw + sign * 1 + max) % max

  override def decrement(): Unit =
    raw = (raw - sign * 1 + max) % max
