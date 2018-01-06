package com.github.mjjaniec.time.application

import java.time.Duration

import com.github.mjjaniec.time.application.persistance.{WorkDayConfig, WorkTimeDao}

object ApplicationConfig {

  def createContext = ApplicationContext(
    new WorkTimeDao("time.db"),
    WorkDayConfig(
      workday = Duration.ofHours(8),
      tolerance = Duration.ofMinutes(10),
      overtimeNotificationInterval = Duration.ofMinutes(30))
  )
}
