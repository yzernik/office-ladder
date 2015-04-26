package officeladder

import scala.concurrent.Future
import scala.util.{ Success, Failure }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom._
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

  case class State(ladders: List[Ladder])

  type ActivationChange = (Long, Boolean) => (ReactEventI) => Unit

  case class Backend($: BackendScope[Unit, State]) {
    def onActivate(id: Long, active: Boolean)(e: ReactEventI) = {
      e.preventDefault()
      alert(s"Activatine ladder: ${id}")
      Ladders.activateLadder(id).onComplete {
        case Success(res) =>
          $.modState(s => State(s.ladders))
          alert(s"Activated ladder ${res.name}.")
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
          LaddersList((S.ladders, B.onActivate)))))
    .componentDidMount(scope => {
      Ladders.fetchAdminLadders.onSuccess {
        case ladders =>
          scope.modState(_ => State(ladders))
      }
    }).buildU

  val LaddersList = ReactComponentB[(List[Ladder], ActivationChange)]("LaddersList")
    .render(P => {
      val (ladders, b) = P
      def ladderListItem(ladder: Ladder) =
        LadderListElement((ladder, b))
      <.ul(ladders map ladderListItem)
    }).build

  val LadderListElement = ReactComponentB[(Ladder, ActivationChange)]("LaddersListElement")
    .render(props => {
      val (ldr, b) = props
      <.li(
        s"${ldr.name}",
        <.br,
        s"domain: ${ldr.domain}",
        <.br,
        s"active: ${ldr.active}",
        <.br,
        <.form(^.onSubmit ==> b(ldr.id, true),
          <.button("Activate ladder")))
    }).build

  val content =
    <.div(
      <.h2("Admin Page"),
      <.div(
        ^.className := "col-lg-8",
        AdminApp()))

}