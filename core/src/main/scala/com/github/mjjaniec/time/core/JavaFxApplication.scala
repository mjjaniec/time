package com.github.mjjaniec.time.core

import javafx.application.{Application, Platform}
import javafx.stage.Stage

class JavaFxApplication extends Application {

  override def start(primaryStage: Stage): Unit = {
    Platform.runLater(() => new QuestionPopup(Daemon.start).show())
  }
}
