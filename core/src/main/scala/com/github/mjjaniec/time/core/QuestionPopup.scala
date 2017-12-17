package com.github.mjjaniec.time.core

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout._

import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class QuestionPopup(onYes: () => Unit) {

  private val notification = new TrayNotification("Czy jesteś w pracy updated :D", "", Notifications.QUESTION)

  {
    val nope = new Button("Nie")
    nope.setOnAction(_ => System.exit(0))

    val yess = new Button("Tak")
    yess.setOnAction(_ => {
      onYes()
      notification.setOnDismiss(_ => ())
      notification.dismiss()
    })

    val buttons = new HBox(12)
    buttons.setPadding(new Insets(8, 0, 0, 0))
    buttons.getChildren.add(yess)
    buttons.getChildren.add(nope)
    yess.setStyle("-fx-background-radius:0;")
    nope.setStyle("-fx-background-radius:0;")

    notification.setCustomContent(buttons)
    notification.setAnimation(Animations.POPUP)
    notification.setOnDismiss(_ => System.exit(0))
  }

  def show(): Unit = notification.showAndWait()
}
