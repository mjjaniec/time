package time

import java.awt.event.{MouseEvent, MouseListener}
import java.awt.image.BufferedImage
import java.awt.{Color, Image, TrayIcon}
import java.time._
import java.util.{Timer, TimerTask}
import javafx.application.Platform

import com.github.plushaze.traynotification.animations.Animations
import com.github.plushaze.traynotification.notification.{Notifications, TrayNotification}

object Daemon {

  private var workdayShowed = false
  private var clearAccountShowed = false
  private var overtimeIteration = 1
  private var firstRun = true
  private var trayIcon: TrayIcon = _

  def start(): Unit = {
    val timer = new Timer()

    timer.schedule(new TimerTask {
      override def run(): Unit = job()
    }, 0, 60 * 1000)
  }

  private def job(): Unit = {
    val data = DataAccess.load()
    val today = LocalDate.now()
    if (today != data.day) {
      val startTime = LocalTime.now().withNano(0).withSecond(0)
      showWorkStartedNotification(startTime, data.toWork.minus(data.worked))
      DataAccess.store(Data(today, startTime, Duration.ZERO, data.toWork.minus(data.worked).plus(Config.Workday)))
    } else {
      val workTime = Duration.between(data.day.atTime(data.started), LocalDateTime.now())
      DataAccess.store(data.copy(worked = workTime))

      if (workTime.isLonger(Config.Workday) && !workdayShowed) {
        val additionalWork = data.worked.minus(data.toWork)
        showWorkdayEnded(additionalWork)
        workdayShowed = true
      } else if (data.worked.isLonger(data.toWork) && data.worked.isEqual(data.toWork, Config.Tolerance) && !clearAccountShowed) {
        showClearAccount(data.worked)
        clearAccountShowed = true
      } else if (data.worked.isLonger(data.toWork.plus(Config.OvertimeInterval.multipliedBy(overtimeIteration)))) {
        showOverwork(data.worked.minus(data.toWork))
        overtimeIteration += 1
      } else if (firstRun) {
        showProgress(data)
      }
    }

    firstRun = false

    val data2 = DataAccess.load()
    refreshTray(data2.worked.getSeconds / data2.toWork.getSeconds.toDouble)
  }



  def showProgress(data: Data): Unit = {
    val line1 = s"Pracujesz już ${printDuration(data.worked)} (od ${data.started})."
    val now = LocalTime.now().withSecond(0).withNano(0)
    val (line2, color) = if (data.worked.isEqual(data.toWork, Config.Tolerance)) {
       s"Masz czyste konto, możesz iść do domu." -> Notifications.SUCCESS
    } else if (data.worked.isLonger(data.toWork)) {
      s"Jesteś już ${printDuration(data.worked.minus(data.toWork))} do przodu, może już idź." -> Notifications.SUCCESS
    } else {
      if (data.toWork.isEqual(Config.Workday, Config.Tolerance)) {
        val toWork = if (Config.Workday.isLonger(data.toWork)) Config.Workday else data.toWork
        val remainng = toWork.minus(data.worked)
        s"Jeszcze tylko ${printDuration(remainng)}. Możesz wyjść o ${now.plus(remainng)}." -> Notifications.INFORMATION
      } else if (data.toWork.isShorter(Config.Workday)) {
        val toClear = data.toWork.minus(data.worked)
        val toWorkday = Config.Workday.minus(data.worked)
        s"Za ${printDuration(toClear)} (${now.plus(toClear)}) czyste konto, " +
          s"za ${printDuration(toWorkday)} (${now.plus(toWorkday)}) dnióweczka." -> Notifications.INFORMATION
      } else {
        val toClear = data.toWork.minus(data.worked)
        val toWorkday = Config.Workday.minus(data.worked)
        //s"${now.plus(toWorkday)} - Za ${printDuration(toWorkday)} dnióweczka" +
          s"Za ${printDuration(toClear)} (${now.plus(toClear)}) odrobisz swoje " +
          s"${printDuration(data.toWork.minus(Config.Workday))} w dupę." ->  Notifications.INFORMATION
      }
    }
    doShowNotification("Status", line1 + "\n" + line2, color)
  }

  private def showOverwork(overwork: Duration): Unit = {
    val msg = s"Jesteś już ${printDuration(overwork)} do przodu, idź już!"

    doShowNotification("Do domu!", msg, Notifications.SUCCESS)
  }

  private def showClearAccount(workTime: Duration): Unit = {
    val msg = s"Twój trud skończony.\n Po ${printDuration(workTime)} pracy masz czyste konto."

    doShowNotification("Do domu!", msg, Notifications.SUCCESS)
  }

  private def showWorkdayEnded(additionalWork: Duration): Unit = {
    val line1 = s"Twój trud skończony."
    val (line2, color) = if (!additionalWork.isEqual(Duration.ZERO, Config.Tolerance)) {
      if (additionalWork.isNegative) {
        s"Ale pamiętaj że jesteś: ${printDuration(additionalWork)} w dupę." -> Notifications.WARNING
      } else {
        s"I jeszcze jesteś: ${printDuration(additionalWork.abs())} do przodu!" -> Notifications.SUCCESS
      }
    } else {
      clearAccountShowed = true
      "Masz czyste konto" -> Notifications.SUCCESS
    }

    doShowNotification("Do domu!", line1 + "\n" + line2, color)
  }

  private def showWorkStartedNotification(startTime: LocalTime, overtime: Duration): Unit = {
    val line1 = s"Pracujesz od: $startTime, kończysz o ${startTime.plus(Config.Workday)}."
    val (line2, color) = if (!overtime.isEqual(Duration.ZERO, Config.Tolerance)) {
      val tHome = startTime.plus(Config.Workday).plus(overtime)
      if (overtime.isNegative) {
        val good = printDuration(overtime.abs())
        s"Jesteś: $good do przodu! Możesz wyjść już o $tHome." -> Notifications.SUCCESS
      } else {
        val loss = printDuration(overtime.abs())
        s"Jesteś $loss w dupę. Może zostań do $tHome." -> Notifications.WARNING
      }
    } else {
      "Masz czyste konto" -> Notifications.INFORMATION
    }

    doShowNotification("Praca na dziś", line1 + "\n" + line2, color)
  }

  private def doShowNotification(title: String, content: String, color: Notifications): Unit = {
    Platform.runLater { () =>
      val n = new TrayNotification(title, content, color)
      n.setAnimation(Animations.POPUP)
      n.showAndWait()
    }
  }

  private def printDuration(duration: Duration): String = {
    duration.toString
      .substring(2)
      .replaceAll("(\\d[HMS])(?!$)", "$1 ")
      .toLowerCase()
  }

  private def refreshTray(progress: Double): Unit = {
    // ensure awt toolkit is initialized.
    java.awt.Toolkit.getDefaultToolkit

    // set up a system tray icon.
    val tray = java.awt.SystemTray.getSystemTray
    val size = tray.getTrayIconSize

    def drawIcon(color: Color): Image = {
      val image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
      val g = image.getGraphics
      val color = if (progress < 1) new Color(50, 190, 240) else new Color(50, 200, 40)
      g.setColor(color)

      val height = ((size.height - 2) * Math.max(0, Math.min(progress, 1))).toInt
      g.drawRect(1, 1, size.width - 2, size.height - 2)
      g.fillRect(1, size.height - 1 - height, size.width - 2, height)
      g.dispose()
      image
    }

    if (trayIcon != null) {
      tray.remove(trayIcon)
    }

    trayIcon = new TrayIcon(drawIcon(Color.RED))

    // if the user double-clicks on the tray icon, show the main app stage.
    trayIcon.addMouseListener(new MouseListener {
      override def mouseExited(e: MouseEvent): Unit = ()
      override def mousePressed(e: MouseEvent): Unit = ()
      override def mouseReleased(e: MouseEvent): Unit = ()
      override def mouseEntered(e: MouseEvent): Unit = ()
      override def mouseClicked(e: MouseEvent): Unit = Daemon.showProgress(DataAccess.load())
    })

    // add the application tray icon to the system tray.
    tray.add(trayIcon)

  }

}
