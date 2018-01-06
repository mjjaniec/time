package com.github.mjjaniec.time.application.persistance

import java.time.{Duration, LocalDate, LocalTime}

case class WorkTimeData(day: LocalDate, started: LocalTime, worked: Duration, toWork: Duration)
