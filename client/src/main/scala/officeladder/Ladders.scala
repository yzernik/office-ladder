package officeladder

import scala.concurrent.Future
import scala.util.{ Success, Failure }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom.ext._

import japgolly.scalajs.react._, vdom.prefix_<^._, ScalazReact._, MonocleReact._, extra._
import monocle.macros._
import upickle._

import models.Ladder

object Ladders {

  case class State(ladders: List[Ladder], name: String)
  case class NewLadder(name: String)

  def fetchLadders: Future[List[Ladder]] = {
    val url = org.scalajs.dom.window.location + "ladders"
    Ajax.get(url).map { res =>
      read[List[Ladder]](res.responseText)
    }
  }

  def createLadder(ldr: NewLadder): Future[Ladder] = {
    val url = org.scalajs.dom.window.location + "ladders"
    val data = write[NewLadder](ldr)
    Ajax.post(url, data, headers = Map("Content-Type" -> "application/json")).map { res =>
      read[Ladder](res.responseText)
    }
  }

  class Backend($: BackendScope[Unit, State]) {

    def onChangeName(e: ReactEventI) =
      $.modState(_.copy(name = e.target.value))

    def handleSubmit(e: ReactEventI) = {
      e.preventDefault()
      val ldr = NewLadder($.state.name)
      createLadder(ldr).onComplete {
        case Success(res) => org.scalajs.dom.alert("New ladder will be created on approval from administrator.")
        case Failure(t)   => org.scalajs.dom.alert("error: " + t.getMessage)
      }
      $.modState(s => State(s.ladders, ""))
    }

  }

  val LaddersList = ReactComponentB[List[Ladder]]("LaddersList")
    .render(props => {
      def createLadderItem(ladder: Ladder) = <.li(ladder.name)
      <.ul(props map createLadderItem)
    })
    .build

  val NewLadderForm = ReactComponentB[(State, Backend)]("NewLadderForm")
    .render(P => {
      val (s, b) = P
      <.form(^.onSubmit ==> b.handleSubmit,
        <.p("Ladder name"),
        <.input(^.onChange ==> b.onChangeName, ^.value := s.name, ^.name := "name"),
        <.br,
        <.button("Add new ladder"))
    })
    .build

  val InputChanger = ReactComponentB[ExternalVar[String]]("Name changer")
    .render { evar =>
      def updateName = (event: ReactEventI) => evar.set(event.target.value)
      <.input(
        ^.`type` := "text",
        ^.value := evar.value,
        ^.onChange ~~> updateName)
    }
    .build

  val LaddersApp = ReactComponentB[Unit]("TodoApp")
    .initialState(State(Nil, ""))
    .backend(new Backend(_))
    .render((_, S, B) =>
      <.div(
        <.div(
          ^.className := "col-lg-6",
          <.h3("Ladders"),
          LaddersList(S.ladders)),
        <.div(
          ^.className := "col-lg-6",
          <.h3("Create a new ladder"),
          NewLadderForm((S, B)))))
    .componentDidMount(scope => {
      fetchLadders.onSuccess {
        case ladders =>
          scope.modState(_ => State(ladders, ""))
      }

    }).buildU

  val content = LaddersApp()
}