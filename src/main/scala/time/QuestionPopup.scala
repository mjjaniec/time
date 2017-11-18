package time

import javafx.geometry.{Insets, Pos}
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.text.Text
import javafx.stage.Stage

class QuestionPopup(onYes: () => Unit) {

  private val dialog = new Stage()

  {
    val nope = new Button("Nie")
    nope.setOnAction(_ => System.exit(0))

    val yess = new Button("Tak")
    yess.setOnAction(_ => {
      onYes()
      dialog.hide()
    })
    val layout = new VBox(12)
    layout.setPrefWidth(200)
    layout.setPadding(new Insets(12))
    layout.getChildren.add(new Text("Czy jesteś w pracy?"))
    val buttons = new HBox(12)
    buttons.getChildren.add(yess)
    buttons.getChildren.add(nope)
    layout.getChildren.add(buttons)
    buttons.setAlignment(Pos.BOTTOM_RIGHT)
    dialog.setScene(new Scene(layout))
    dialog.setResizable(false)
    dialog.setAlwaysOnTop(true)
  }


  def show(): Unit = dialog.show()
}
