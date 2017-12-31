package com.github.mjjaniec.time.application
package popup

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout._
import javafx.util.Duration

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
    }.asJava

    onDismiss.foreach(action => notification.setOnDismiss(_ => action()))

    val buttonsLayout = new HBox(12)
    buttonsLayout.setPadding(new Insets(8, 0, 0, 0))
    buttonsLayout.getChildren.addAll(buttons)

    notification.setCustomContent(buttonsLayout)
    notification.setAnimation(Animations.POPUP)
  }

  def show(): Unit = notification.showAndWait()

  def showAndDismiss(dismissDelay: Duration): Unit = notification.showAndDismiss(dismissDelay)
}
