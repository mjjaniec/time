package com.github.mjjaniec.time

import java.awt.MenuItem
import java.time.Duration
import javafx.application.Platform

import com.avsystem.commons.collection.CollectionAliases
import com.avsystem.commons.jiop.JavaInterop
import com.avsystem.commons.{CommonAliases, SharedExtensions}

package object application extends SharedExtensions with CommonAliases with CollectionAliases with JavaInterop {

  implicit class DurationEx(val self: Duration) extends AnyVal {
    def isShorter(other: Duration): Boolean = self.minus(other).isNegative
    def isLonger(other: Duration): Boolean = other.minus(self).isNegative
    def isEqual(other: Duration, tolerance: Duration): Boolean =
      self.abs().minus(other.abs()).abs().minus(tolerance.abs()).isNegative
  }

  implicit class MenuItemEx(val self: MenuItem) extends AnyVal {
    def setAction(action: () => Unit): Unit = {
      self.addActionListener(_ => Platform.runLater(() => action()))
    }
  }
}
