package officeladder

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

import org.scalajs.dom.ext._

import japgolly.scalajs.react._, vdom.prefix_<^._
import upickle.SeqishR
import upickle.read

import models.Ladder

object Ladders {

  case class State(ladders: List[Ladder], newladder: NewLadder)
  case class NewLadder(name: String, domain: String)

  def fetchLadders: Future[List[Ladder]] = {
    val url = org.scalajs.dom.window.location + "ladders"
    Ajax.get(url).map { res =>
      read[List[Ladder]](res.responseText)
    }
  }

  class Backend($: BackendScope[Unit, State]) {
    // add some methods..
  }

  val LaddersList = ReactComponentB[List[Ladder]]("LaddersList")
    .render(props => {
      def createLadderItem(ladder: Ladder) = <.li(ladder.name)
      <.ul(props map createLadderItem)
    })
    .build

  val LaddersApp = ReactComponentB[Unit]("TodoApp")
    .initialState(State(Nil, NewLadder("", "")))
    .backend(new Backend(_))
    .render((_, S, B) =>
      <.div(
        <.h3("Ladders"),
        LaddersList(S.ladders)))
    .componentDidMount(scope => {
      fetchLadders.onSuccess {
        case ladders =>
          org.scalajs.dom.alert("num ladders: " + ladders.size)
          scope.modState(_ => State(ladders, NewLadder("", "")))
      }

    }).buildU

  val content = LaddersApp()
}