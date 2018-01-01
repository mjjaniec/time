package com.github.mjjaniec.time.application.util.modifiers

import javafx.geometry.Pos
import javafx.scene.layout.HBox

class HBoxModifier[T <: HBox](box: T) extends LayoutModifier[T](box) {
  def spacing: this.type = {
    box.setSpacing(10)
    this
  }

  def alignment(alignment: Pos): this.type = {
    box.setAlignment(alignment)
    this
  }
}
