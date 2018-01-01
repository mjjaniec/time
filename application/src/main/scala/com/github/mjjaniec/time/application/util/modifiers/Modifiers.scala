package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.{Button, ComboBox, Label, Spinner}
import javafx.scene.layout.{HBox, VBox}

object Modifiers {
  def hBox[T <: HBox](hBox: T): HBoxModifier[T] = new HBoxModifier[T](hBox)
  def vBox[T <: VBox](vBox: T): VBoxModifier[T] = new VBoxModifier[T](vBox)
  def label[T <: Label](label: T): LabelModifier[T] = new LabelModifier[T](label)
  def comboBox[I, T <: ComboBox[I]](comboBox: T): ComboBoxModifier[I, T] = new ComboBoxModifier[I, T](comboBox)
  def button[T <: Button](button: T): ButtonModifier[T] = new ButtonModifier[T](button)
  def spinner[I, T <: Spinner[I]](spinner: T): SpinnerModifier[I, T] = new SpinnerModifier[I, T](spinner)
}
