package com.github.mjjaniec.time.application

import javafx.application.{Application, Platform}

object Main {

  def main(args: Array[String]): Unit = {
    Platform.setImplicitExit(false)
    Application.launch(classOf[JavaFxApplication], args: _*)
  }

}
