package com.github.mjjaniec.time.application
package tray

import java.awt.{MenuItem, PopupMenu}
import javafx.util.Duration

import com.github.mjjaniec.time.application.popup.{QuestionOption, QuestionPopup}
import com.github.mjjaniec.time.application.view.AdjustOvertimeView

class AppTrayMenu extends PopupMenu {

  {
    add(new MenuItem("Status pracy").setup(_.setAction(() => Daemon.showProgress(DataAccess.load()))))
    add(new MenuItem("Dostosuj czas pracy").setup(_.setAction(() =>
      new AdjustOvertimeView(
        Config.Workday minus DataAccess.load().toWork,
        duration => {
          val current = DataAccess.load()
          val updated = current.copy(toWork = Config.Workday minus duration)
          DataAccess.store(updated)
          Daemon.showProgress(updated)
        }
      ).show())))
    addSeparator()
    add(new MenuItem("Wyjście").setup(_.setAction(onExit)))
  }

  private def onExit(): Unit = {
    val popup = new QuestionPopup(
      "Twój czas pracy nie będzie już liczony.",
      Vector(QuestionOption("Hej, jeszcze pracuję", () => ())),
      Opt(() => System.exit(0))
    )

    popup.showAndDismiss(Duration.seconds(10))
  }

}
