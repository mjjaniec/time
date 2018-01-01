package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.Node

abstract class NodeModifier[T <: Node](val node: T) {
  def get: T = node
}
