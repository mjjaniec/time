package com.github.mjjaniec.time.application.util

import javafx.scene.Node
import javafx.scene.control.{Button, ComboBox, Label, Spinner}
import javafx.scene.layout.{HBox, VBox}

import com.github.mjjaniec.time.application.util.modifiers._

import scala.reflect.ClassTag

object Creators {
  def hBox: HBoxModifier[HBox] = Modifiers.hBox(new HBox)
  def vBox: VBoxModifier[VBox] = Modifiers.vBox(new VBox)

  def label: LabelModifier[Label] = Modifiers.label(new Label)
  def button: ButtonModifier[Button] = Modifiers.button(new Button)

  def comboBox[I: ClassTag]: ComboBoxModifier[I, ComboBox[I]] = Modifiers.comboBox[I, ComboBox[I]](new ComboBox[I]())
  def spinner(min: Int, max: Int, start: Int): SpinnerModifier[Int, Spinner[Int]] = Modifiers.spinner[Int, Spinner[Int]](new Spinner[Int](min, max, start))

  def hSpace: Node = label.width(LayoutModifier.BaseSpace).get
  def vSpace: Node = label.height(LayoutModifier.BaseSpace).get
}

