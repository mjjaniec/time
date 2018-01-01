package com.github.mjjaniec.time.application
package popup

import javafx.scene.control.Button
import javafx.util.Duration

import com.github.mjjaniec.time.application.util.GUI
import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

case class QuestionOption(caption: String, action: () => Unit, dismissAfterAction: Boolean = true)

class QuestionPopup(question: String, options: Vector[QuestionOption], onDismiss: Opt[() => Unit] = Opt.empty) {

  private val notification = new TrayNotification(question, "", Notifications.QUESTION)

  {
    val buttons = options.map { case QuestionOption(caption, action, dismissAfterAction) =>
      val b = new Button(caption)
      b.setOnAction(_ => {
        action.apply()
        if (dismissAfterAction) {
          notification.setOnDismiss(_ => ())
          notification.dismiss()
        }
      })
      b.setStyle("-fx-background-radius:0;")
      b
    }

    onDismiss.foreach(action => notification.setOnDismiss(_ => action()))

    GUI.create.hBox.spacing.padded(top = true, right = false, bottom = false, left = false)
      .addChildren(buttons).get |> notification.setCustomContent

    notification.setAnimation(Animations.POPUP)
  }

  def show(): Unit = notification.showAndWait()

  def showAndDismiss(dismissDelay: Duration): Unit = notification.showAndDismiss(dismissDelay)
}
