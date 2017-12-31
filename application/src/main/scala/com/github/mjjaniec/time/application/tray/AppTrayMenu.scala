package com.github.mjjaniec.time.application
package tray

import java.awt.{MenuItem, PopupMenu}
import javafx.application.Platform
import javafx.util.Duration

import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

class AppTrayMenu extends PopupMenu {

  {
    add(new MenuItem("Status pracy").setup(_.addActionListener(_ => Daemon.showProgress(DataAccess.load()))))
    addSeparator()
    add(new MenuItem("Wyjście").setup(_.addActionListener(_ => onExit())))
  }

  private def onExit(): Unit = {
    Platform.runLater(() => {
      var doExit = true
      val notification = new TrayNotification()
      notification.setTitle("Trzym się :)")
      notification.setMessage("Twój czas pracy nie będzie już liczony")
      notification.setNotification(Notifications.QUESTION)
      notification.setAnimation(Animations.POPUP)
      notification.setOnDismiss(_ =>
       System.exit(0)
      )
      notification.showAndDismiss(Duration.seconds(10))
    })


  }

}
