package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.Label

class LabelModifier[T <: Label](label: T) extends LabelledModifier[T](label)  {
  def text(value: String): this.type = label(value)
}
