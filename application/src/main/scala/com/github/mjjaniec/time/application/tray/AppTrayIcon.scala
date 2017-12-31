package com.github.mjjaniec.time.application.tray

import java.awt.event.{MouseEvent, MouseListener}
import java.awt.image.BufferedImage
import java.awt._


class AppTrayIcon(popupMenu: PopupMenu, onClick: () => Unit) {

  {
    // ensure awt toolkit is initialized.
    java.awt.Toolkit.getDefaultToolkit
  }

  private val pixelSize = SystemTray.getSystemTray.getTrayIconSize
  private val trayIcon = new TrayIcon(drawTrayIconIcon())

  private var pixelProgress: Int = -1
  private var progress: Double = 0

  {
    trayIcon.addMouseListener(new AppTrayIcon.TrayIconClickListener(onClick))
    trayIcon.setPopupMenu(popupMenu)
  }

  def show(): Unit = SystemTray.getSystemTray.add(trayIcon)

  def hide(): Unit = SystemTray.getSystemTray.remove(trayIcon)


  def update(progress: Double, tooltip: String): Unit = {
    this.progress = progress
    trayIcon.setToolTip(tooltip)

    val newPixelProgress = ((pixelSize.height - 2) * Math.max(0, Math.min(progress, 1))).toInt
    if (newPixelProgress != pixelProgress) {
      pixelProgress = newPixelProgress
      trayIcon.setImage(drawTrayIconIcon())
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
}

object AppTrayIcon {

  class TrayIconClickListener(onClick: () => Unit) extends MouseListener() {
    override def mouseExited(e: MouseEvent): Unit = ()

    override def mousePressed(e: MouseEvent): Unit = ()

    override def mouseReleased(e: MouseEvent): Unit = ()

    override def mouseEntered(e: MouseEvent): Unit = ()

    override def mouseClicked(e: MouseEvent): Unit = {
      if (e.getButton == MouseEvent.BUTTON1) {
        onClick()
      }
    }
  }

}
