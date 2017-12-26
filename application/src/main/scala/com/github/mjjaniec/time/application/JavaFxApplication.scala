package com.github.mjjaniec.time.application

import javafx.application.{Application, Platform}
import javafx.stage.Stage

import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class JavaFxApplication extends Application {

  override def start(primaryStage: Stage): Unit = {
    if (getParameters.getUnnamed.size() == 2) {
      val version = getParameters.getUnnamed.get(0)
      val changeLog = getParameters.getUnnamed.get(1)
      Platform.runLater(() => {
        val notification = new TrayNotification()
        notification.setTitle("Updated to version: " + version)
        notification.setMessage(changeLog)
        notification.setNotification(Notifications.SUCCESS)
        notification.setAnimation(Animations.POPUP)
        notification.setOnDismiss(_ => run())
        notification.showAndWait()
      })
    } else {
      run()
    }
  }

  private def run(): Unit = {
    Platform.runLater(() => new QuestionPopup(Daemon.start).show())
  }
}
