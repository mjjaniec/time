package time

import javafx.geometry.{Insets, Pos}
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout._
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage

import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class QuestionPopup(onYes: () => Unit) {

  private val notification = new TrayNotification("Czy jesteś w pracy", "", Notifications.INFORMATION)

  {
    val nope = new Button("Nie")
    nope.setOnAction(_ => System.exit(0))

    val yess = new Button("Tak")
    yess.setOnAction(_ => {
      onYes()
      notification.dismiss()
    })

    val buttons = new HBox(12)
    buttons.getChildren.add(yess)
    buttons.getChildren.add(nope)

    notification.setCustomContent(buttons)
    notification.setAnimation(Animations.POPUP)
  }


  def show(): Unit = notification.showAndWait()
}
