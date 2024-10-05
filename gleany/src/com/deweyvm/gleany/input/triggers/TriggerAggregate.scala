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

package com.deweyvm.gleany.input.triggers

import com.deweyvm.gleany.input.Control
import com.deweyvm.gleany.GleanyMath

class TriggerAggregate(triggers: Seq[Trigger]) extends Control[Boolean]:
  private var prevFlag = 0
  private var flag = 0

  override def update(): Unit =
    prevFlag = flag
    if triggers.exists(_.isPressed) then
      flag = GleanyMath.clamp(flag + 1, 0, Int.MaxValue - 1)
    else flag = 0

  override def isPressed: Boolean = flag > 0
  override def justPressed: Boolean = flag == 1
  override def justReleased: Boolean = prevFlag != 0 && flag == 0
  override def zip(start: Int, num: Int): Boolean =
    justPressed || (flag > start && flag % num == 0)
