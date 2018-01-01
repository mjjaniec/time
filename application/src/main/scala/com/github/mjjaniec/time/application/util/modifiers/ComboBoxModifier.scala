package com.github.mjjaniec.time.application
package util.modifiers

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox

class ComboBoxModifier[I, T <: ComboBox[I]](combo: T) extends RegionModifier[T](combo) {

  def items(opts: Iterable[I]): this.type = {
    combo.setItems(FXCollections.observableList(opts.toList.asJava))
    this
  }

  def value(item: I): this.type = {
    combo.setValue(item)
    this
  }

}
