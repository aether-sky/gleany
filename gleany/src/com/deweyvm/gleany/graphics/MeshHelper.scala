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

package com.deweyvm.gleany.graphics

import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.VertexAttributes.Usage

object MeshHelper:
  def makeMesh: Mesh = 
    val mesh = new Mesh(true, 4, 0,
      new VertexAttribute(Usage.Position, 3, "a_position"),
      new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"))
    val vertices = Array[Float](-1f, -1f, 0, 0, 1,
                                 1f, -1f, 0, 1, 1,
                                 1f,  1f, 0, 1, 0,
                                -1f,  1f, 0, 0, 0)
    mesh.setVertices(vertices)
    mesh
