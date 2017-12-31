package com.github.mjjaniec.time.application.view

import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.stage.Stage

class AdjustOvertimeView {
  val stage = new Stage()

  {
    val layout = new HBox()
    val scene = new Scene(layout)
    stage.setScene(scene)
  }

  def show(): Unit = {
    stage.show()
  }

}
