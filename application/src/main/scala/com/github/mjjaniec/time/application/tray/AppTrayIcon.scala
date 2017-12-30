package com.github.mjjaniec.time.application.tray

import java.awt.event.{MouseEvent, MouseListener}
import java.awt.image.BufferedImage
import java.awt.{Color, Image, SystemTray, TrayIcon}

import com.github.mjjaniec.time.application.{Daemon, DataAccess}

class AppTrayIcon(private var progress: Double) {

  private val tray = SystemTray.getSystemTray
  private val pixelSize = tray.getTrayIconSize

  private var pixelProgress: Int = -1
  private var trayIcon: TrayIcon = _

  {
    // ensure awt toolkit is initialized.
    java.awt.Toolkit.getDefaultToolkit
    update(progress)
  }

  def update(progress: Double): Unit = {
    this.progress = progress

    val newPixelProgress = ((pixelSize.height - 2) * Math.max(0, Math.min(progress, 1))).toInt
    if (newPixelProgress != pixelProgress) {
      pixelProgress = newPixelProgress
      refreshTrayIcon()
    }
  }

  private def drawTrayIconIcon(): Image = {
    val w = pixelSize.width
    val h = pixelSize.height
    val image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val g = image.getGraphics
    val color = if (progress < 1) new Color(50, 190, 240) else new Color(50, 200, 40)
    g.setColor(color)


    g.drawRect(0, 0, w - 1, h - 1)
    g.fillRect(0, h - 1 - pixelProgress, w - 1, h - 1)
    g.dispose()
    image
  }

  private def refreshTrayIcon(): Unit = {
    if (trayIcon != null) {
      tray.remove(trayIcon)
    }

    trayIcon = new TrayIcon(drawTrayIconIcon())

    // if the user double-clicks on the tray icon, show the main app stage.
    trayIcon.addMouseListener(new MouseListener {
      override def mouseExited(e: MouseEvent): Unit = ()

      override def mousePressed(e: MouseEvent): Unit = ()

      override def mouseReleased(e: MouseEvent): Unit = ()

      override def mouseEntered(e: MouseEvent): Unit = ()

      override def mouseClicked(e: MouseEvent): Unit = {
        Daemon.showProgress(DataAccess.load())
      }
    })

    // add the application tray icon to the system tray.
    tray.add(trayIcon)
  }

}
