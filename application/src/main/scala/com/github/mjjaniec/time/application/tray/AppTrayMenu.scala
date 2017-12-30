package com.github.mjjaniec.time.application
package tray

import java.awt.{MenuItem, PopupMenu}

class AppTrayMenu extends PopupMenu {

  {
    add(new MenuItem("Status pracy").setup(_.addActionListener(_ => Daemon.showProgress(DataAccess.load()))))
    addSeparator()
    add(new MenuItem("Wyjście").setup(_.addActionListener(_ => System.exit(0))))
  }

}
