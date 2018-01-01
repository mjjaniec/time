package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.Labeled

class LabelledModifier[T<: Labeled](labelled: T) extends ControlModifier[T](labelled) {

  def label(label: String): this.type = {
    labelled.setText(label)
    this
  }

}
