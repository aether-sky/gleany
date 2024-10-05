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

package com.deweyvm.gleany.audio

import collection.mutable.ArrayBuffer
import com.deweyvm.gleany.Debug
import com.deweyvm.gleany.saving.AudioSettings

class ActiveAudioCollection:
  private var playing = ArrayBuffer[AudioInstance]()
  private var paused = false

  def +=(audio: AudioInstance): Unit =
    playing += audio

  def update(): Unit =
    if !paused then playing = playing filter { _.isPlaying }

  def pause(): Unit =
    if !paused then
      paused = true
      playing foreach { _.pause() }

  def resume(): Unit =
    if paused then
      paused = false
      playing foreach { _.resume() }

class AudioManager(val settings: AudioSettings):
  Debug.load()
  // fixme -- code clones
  private val playingSfx = new ActiveAudioCollection
  private val playingMusic = new ActiveAudioCollection
  private var sfxVolume = settings.getSfxVolume
  private var musicVolume = settings.getMusicVolume
  private val allAudio = ArrayBuffer[AudioInstance]()

  def +=(audio: Sfx): Unit =
    playingSfx += audio

  def +=(audio: Music): Unit =
    playingMusic += audio

  def register(audio: AudioInstance): Unit =
    allAudio += audio

  def update(): Unit =
    playingSfx.update()
    playingMusic.update()

  def setSfxVolume(newVolume: Float): Unit =
    sfxVolume = newVolume
    allAudio filter {
      _.isInstanceOf[Sfx]
    } foreach {
      _.setVolume(newVolume)
    }

  def setMusicVolume(newVolume: Float): Unit =
    musicVolume = newVolume
    allAudio filter {
      _.isInstanceOf[Music]
    } foreach {
      _.setVolume(newVolume)
    }

  def pauseSfx(): Unit =
    playingSfx.pause()

  def resumeSfx(): Unit =
    playingSfx.resume()

  def pauseMusic(): Unit =
    playingMusic.pause()

  def resumeMusic(): Unit =
    playingMusic.resume()

  def pause(): Unit =
    pauseMusic()
    pauseSfx()

  def resume(): Unit =
    resumeMusic()
    resumeSfx()
