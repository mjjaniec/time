package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.layout.Region

class RegionModifier[T <: Region](region: T) extends NodeModifier[T](region) {

  def width(w: Double): this.type = {
    region.setPrefWidth(w)
    this
  }

  def height(h: Double): this.type = {
    region.setPrefHeight(h)
    this
  }

}
