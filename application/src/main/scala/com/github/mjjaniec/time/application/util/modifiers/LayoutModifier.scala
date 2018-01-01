package com.github.mjjaniec.time.application
package util.modifiers

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.layout.Pane

class LayoutModifier[T <: Pane](layout: T) extends RegionModifier[T](layout) {

  import LayoutModifier._

  def padded: this.type = {
    layout.setPadding(new Insets(BaseSpace))
    this
  }

  def padded(top: Boolean, right: Boolean, bottom: Boolean, left: Boolean): this.type = {
    def v(boolean: Boolean) = if (boolean) BaseSpace else 0

    layout.setPadding(new Insets(v(top), v(right), v(bottom), v(left)))
    this
  }

  def addChild(child: Node): this.type = {
    layout.getChildren.add(child)
    this
  }

  def addChildren(children: Iterable[Node]): this.type = {
    layout.getChildren.addAll(children.toVector.asJava)
    this
  }

}

object LayoutModifier {
  val BaseSpace = 10
}




