package officeladder

import scala.concurrent.Future
import scala.util.{ Success, Failure }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom.ext._

import japgolly.scalajs.react._, vdom.prefix_<^._, ScalazReact._, MonocleReact._, extra._
import japgolly.scalajs.react.vdom.all.div
import japgolly.scalajs.react.vdom.all.`class`
import monocle.macros._
import upickle._

import models._

object Admin {

  val container = ReactComponentB[Unit]("adminPage")
    .render(P => {
      div(`class` := "container", content)
    }).buildU

  @Lenses
  case class State(ladders: List[Ladder])

  case class Backend($: BackendScope[Unit, State]) {
    def activate(e: ReactEventI) = {
      e.preventDefault()
      e.target.getAttribute("id").asInstanceOf[Long]
      Ladders.activateLadder(12345L).onComplete {
        case Success(res) =>
          $.modState(s => State(s.ladders))
          org.scalajs.dom.alert(s"Activated ladder ${res.name}.")
        case Failure(t) => org.scalajs.dom.alert("error: " + t.getMessage)
      }
    }
  }

  val AdminApp = ReactComponentB[Unit]("AdminApp")
    .initialState(State(Nil))
    .backend(new Backend(_))
    .render((_, S, B) =>
      <.div(
        <.div(
          ^.className := "col-lg-6",
          <.h3("Ladders"),
          LaddersList(S.ladders))))
    .componentDidMount(scope => {
      Ladders.fetchAdminLadders.onSuccess {
        case ladders =>
          scope.modState(_ => State(ladders))
      }
    }).buildU

  val LaddersList = ReactComponentB[List[Ladder]]("LaddersList")
    .render(props => {
      def ladderListItem(ladder: Ladder) = <.li(
        s"${ladder.name}, domain: ${ladder.domain}")
      <.ul(props map ladderListItem)
    }).build

  val content =
    <.div(
      <.h2("Admin Page"),
      <.div(
        ^.className := "col-lg-8",
        AdminApp()))

}