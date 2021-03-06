package com.github.mjjaniec.time.application.persistance

import java.io._
import java.time.{Duration, LocalDate, LocalTime}
import java.util.Scanner

import scala.util.control.NonFatal


class WorkTimeDao(filename: String) {

  def store(data: WorkTimeData): Unit = {
    val writer = new FileWriter(filename)
    writer.write(s"day: ${data.day.toString}\n")
    writer.write(s"started: ${data.started.toString}\n")
    writer.write(s"worked: ${data.worked.toMinutes}m\n")
    writer.write(s"to_work: ${data.toWork.toMinutes}m\n")
    writer.flush()
    writer.close()
  }

  def load(): WorkTimeData = {
    val f = new File(filename)
    if (f.exists()) {
      val scanner = new Scanner(new FileReader(f))
      val dayPattern = "day: (\\d+)-(\\d+)-(\\d+)".r
      val timePattern = "started: (\\d+):(\\d+).*".r
      val workedPattern = "worked: (\\d+)m".r
      val toWorkPattern = "to_work: (\\d+)m".r
      try {
        val line1 = scanner.nextLine()
        val line2 = scanner.nextLine()
        val line3 = scanner.nextLine()
        val line4 = scanner.nextLine()
        val day = line1 match {
          case dayPattern(year, month, dayOfMonth) => LocalDate.of(year.toInt, month.toInt, dayOfMonth.toInt)
        }
        val time = line2 match {
          case timePattern(hour, minute) => LocalTime.of(hour.toInt, minute.toInt)
        }
        val worked = line3 match {
          case workedPattern(minutes) => Duration.ofMinutes(minutes.toInt)
        }
        val toWork = line4 match {
          case toWorkPattern(minutes) => Duration.ofMinutes(minutes.toInt)
        }
        WorkTimeData(day, time, worked, toWork)
      } catch {
        case NonFatal(_) => default
      } finally {
        scanner.close()
      }
    } else {
      default
    }
  }

  private def default: WorkTimeData = WorkTimeData(LocalDate.MIN, LocalTime.MIN, Duration.ZERO, Duration.ZERO)
}
