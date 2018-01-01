package com.github.mjjaniec.time.application
package view

import java.time.Duration
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Stage

import com.avsystem.commons.misc.{NamedEnum, NamedEnumCompanion}
import com.github.mjjaniec.time.application.util.GUI

object AdjustOvertimeView {

  sealed abstract class TimeType(override val name: String) extends NamedEnum

  object TimeType extends NamedEnumCompanion[TimeType] {

    object Overtime extends TimeType("Nadgodzinki")

    object Undertime extends TimeType("W plecki")

    override val values: Seq[TimeType] = caseObjects
  }

}

class AdjustOvertimeView(currentOvertime: Duration, saveOvertime: Duration => Unit) {

  import AdjustOvertimeView._

  private val stage = new Stage()
  private val hours = GUI.create.spinner(0, Int.MaxValue, 0).editable.get
  private val minutes = GUI.create.spinner(0, 59, 0).editable.get
  private val timeType = GUI.create.comboBox[TimeType].items(TimeType.values).value(TimeType.Overtime).get

  {
    stage.sizeToScene()
    stage.setResizable(false)
  }

  def show(): Unit = {
    val scene = new Scene(constructLayout)
    stage.setScene(scene)
    stage.setTitle("Dostosuj czas pracy")
    stage.getIcons.add(new Image(getClass.getClassLoader.getResource("images/clock.png").toString))
    stage.show()
    loadOvertime(currentOvertime)
  }

  private def constructLayout: Pane = {
    GUI.create.vBox
      .padded.spacing
      .addChild(GUI.create.label.text("Dzień zaczął się z:").get)
      .addChild(GUI.create.hBox
        .alignment(Pos.BASELINE_LEFT)
        .addChild(hours)
        .addChild(GUI.create.label.text(" h").get)
        .addChild(GUI.create.hSpace)
        .addChild(minutes)
        .addChild(GUI.create.label.text(" m").get)
        .addChild(GUI.create.hSpace)
        .addChild(timeType)
        .get)
      .addChild(GUI.create.vSpace)
      .addChild(GUI.create.hBox.alignment(Pos.BOTTOM_RIGHT)
        .spacing
        .addChild(GUI.create.button.primary.label("Zapisz").onClick {
          saveOvertime(readOvertime)
          stage.close()
        }.get)
        .addChild(GUI.create.button.label("Anuluj").onClick(stage.close()).get)
        .get)
      .get
  }

  private def loadOvertime(overtime: Duration): Unit = {
    val h = overtime.abs.toHours
    val m = overtime.abs.minus(Duration.ofHours(h)).toMinutes
    hours.getValueFactory.setValue(h.toInt)
    minutes.getValueFactory.setValue(m.toInt)
    timeType.setValue(if (overtime.isNegative) TimeType.Undertime else TimeType.Overtime)
  }

  private def readOvertime: Duration = {
    val absolute = Duration.ofHours(hours.getValue).plus(Duration.ofMinutes(minutes.getValue))
    timeType.getValue match {
      case TimeType.Overtime => absolute
      case TimeType.Undertime => absolute.negated()
    }
  }

}
