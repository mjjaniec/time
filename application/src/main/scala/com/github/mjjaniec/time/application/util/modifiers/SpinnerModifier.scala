package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.{Spinner, SpinnerValueFactory}

class SpinnerModifier[I, T <: Spinner[I]](spinner: T) extends ControlModifier[T](spinner) {

  def valueFactory(valueFactory: SpinnerValueFactory[I]): this.type = {
    spinner.setValueFactory(valueFactory)
    this
  }

  def editable(editable: Boolean): this.type = {
    spinner.setEditable(editable)
    this
  }

  def editable: this.type = editable(true)


}
