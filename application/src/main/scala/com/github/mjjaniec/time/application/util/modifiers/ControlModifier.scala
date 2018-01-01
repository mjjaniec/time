package com.github.mjjaniec.time.application.util.modifiers

import javafx.scene.control.Control

class ControlModifier[T <: Control](control: T) extends RegionModifier[T](control) {

}
