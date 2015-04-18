package officeladder

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

import org.scalajs.dom.ext._

import japgolly.scalajs.react._, vdom.prefix_<^._
import upickle.SeqishR
import upickle.read

import models.Ladder

object Ladders {

  case class State(ladders: List[Ladder], name: String, domain: String)
  case class NewLadder(name: String, domain: String)

  def fetchLadders: Future[List[Ladder]] = {
    val url = org.scalajs.dom.window.location + "ladders"
    Ajax.get(url).map { res =>
      read[List[Ladder]](res.responseText)
    }
  }

  class Backend($: BackendScope[Unit, State]) {
    def onChangeName(e: ReactEventI) =
      $.modState(_.copy(name = e.target.value))
    def onChangeDomain(e: ReactEventI) =
      $.modState(_.copy(domain = e.target.value))
    def handleSubmit(e: ReactEventI) = {
      e.preventDefault()
      $.modState(s => State(s.ladders, "", ""))
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
        <.p("Ladder domain"),
        <.input(^.onChange ==> b.onChangeDomain, ^.value := s.domain, ^.name := "domain"),
        <.br,
        <.button("Add new ladder"))
    })
    .build

  val LaddersApp = ReactComponentB[Unit]("TodoApp")
    .initialState(State(Nil, "", ""))
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
          org.scalajs.dom.alert("num ladders: " + ladders.size)
          scope.modState(_ => State(ladders, "", ""))
      }

    }).buildU

  val content = LaddersApp()
}