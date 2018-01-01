package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.Button

class ButtonModifier[T <: Button](button: T) extends LabelledModifier[T](button) {

  def primary: this.type = {
    button.setDefaultButton(true)
    this
  }

  def onClick(action: => Unit): this.type = {
    button.setOnAction(_ => action)
    this
  }

}