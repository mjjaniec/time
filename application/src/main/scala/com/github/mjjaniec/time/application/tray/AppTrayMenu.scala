package com.github.mjjaniec.time.application
package tray

import java.awt.{MenuItem, PopupMenu}
import javafx.util.Duration

import com.github.mjjaniec.time.application.persistance.{WorkDayConfig, WorkTimeDao}
import com.github.mjjaniec.time.application.popup.{QuestionOption, QuestionPopup}
import com.github.mjjaniec.time.application.view.AdjustOvertimeView

class AppTrayMenu(applicationContext: ApplicationContext, daemon: Daemon) extends PopupMenu {
  private val workday = applicationContext.workDayConfig.workday
  private val workTimeDao = applicationContext.workTimeDao

  {
    add(new MenuItem("Status pracy").setup(_.setAction(() => daemon.showProgress(workTimeDao.load()))))
    add(new MenuItem("Dostosuj czas pracy").setup(_.setAction(() =>
      new AdjustOvertimeView(
        workday minus workTimeDao.load().toWork,
        duration => {
          val current = workTimeDao.load()
          val updated = current.copy(toWork = workday minus duration)
          workTimeDao.store(updated)
          daemon.showProgress(updated)
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
