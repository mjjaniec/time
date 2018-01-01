package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.layout.VBox

class VBoxModifier[T <: VBox](box: T) extends LayoutModifier[T](box) {
  def spacing: this.type = {
    box.setSpacing(10)
    this
  }
}
