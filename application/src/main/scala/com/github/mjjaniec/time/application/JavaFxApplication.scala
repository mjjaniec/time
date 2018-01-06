package com.github.mjjaniec.time.application

import javafx.application.{Application, Platform}
import javafx.stage.Stage

import com.github.mjjaniec.time.application.popup.{QuestionOption, QuestionPopup}
import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class JavaFxApplication extends Application {

  private val applicationContext = ApplicationConfig.createContext
  private val daemon = new Daemon(applicationContext)

  override def start(primaryStage: Stage): Unit = {
    getParameters.getUnnamed.size() match {
      case 0 => showStartupQuestion()
      case 1 => daemon.start()
      case 2 => showUpdateNotification(getParameters.getUnnamed.get(0), getParameters.getUnnamed.get(1))
    }
  }

  override def stop(): Unit = daemon.stop()

  private def showUpdateNotification(version: String, changelog: String): Unit = {
    Platform.runLater(() => {
      val notification = new TrayNotification()
      notification.setTitle("Zaktualizowano do wersji: " + version)
      notification.setMessage(changelog)
      notification.setNotification(Notifications.SUCCESS)
      notification.setAnimation(Animations.POPUP)
      notification.setOnDismiss(_ => showStartupQuestion())
      notification.showAndWait()
    })
  }


  private def showStartupQuestion(): Unit = {
    new QuestionPopup(
      "Czy jesteś w pracy?",
      Vector(
        QuestionOption("Tak", () => daemon.start()),
        QuestionOption("Nie", () => System.exit(0))
      ),
      Opt(() => System.exit(0))
    ).show()
  }
}
