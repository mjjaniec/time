package com.github.mjjaniec.time.core

import javafx.application.{Application, Platform}
import javafx.stage.Stage

import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class JavaFxApplication extends Application {

  override def start(primaryStage: Stage): Unit = {
    if (getParameters.getUnnamed.size() == 2) {
      val version = getParameters.getUnnamed.get(0)
      val changeLog = getParameters.getUnnamed.get(1)
      Platform.runLater(() => new TrayNotification("Updated to version: " + version, changeLog, Notifications.SUCCESS).showAndWait())
    }
    Platform.runLater(() => new QuestionPopup(Daemon.start).show())
  }
}
