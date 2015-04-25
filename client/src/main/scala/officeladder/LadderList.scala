package officeladder

import scala.concurrent.Future
import scala.util.{ Success, Failure }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom.ext._

import japgolly.scalajs.react._, vdom.prefix_<^._, ScalazReact._, MonocleReact._, extra._
import monocle.macros._
import upickle._

import models._

object LadderList {

  @Lenses
  case class State(ladders: List[Ladder], LadderInput: LadderInput)

  val inputLens = GenLens[LadderInput]

  case class Backend($: BackendScope[Unit, State]) {
    def handleSubmit(e: ReactEventI) = {
      e.preventDefault()
      val ldr = $.state.LadderInput
      Ladders.createLadder(ldr).onComplete {
        case Success(res) => org.scalajs.dom.alert(s"New ladder ${res.name} will be created on approval from administrator.")
        case Failure(t)   => org.scalajs.dom.alert("error: " + t.getMessage)
      }
      $.modState(s => State(s.ladders, LadderInput("")))
    }
  }

  val LaddersList = ReactComponentB[List[Ladder]]("LaddersList")
    .render(props => {
      def ladderListItem(ladder: Ladder) = <.li(ladder.name)
      <.ul(props map ladderListItem)
    }).build

  val InputChanger = ReactComponentB[ExternalVar[String]]("Input changer")
    .render { evar =>
      def updateName = (event: ReactEventI) => evar.set(event.target.value)
      <.input(
        ^.`type` := "text",
        ^.value := evar.value,
        ^.onChange ~~> updateName)
    }.build

  val LadderInputForm = ReactComponentB[(State, Backend)]("LadderInputForm")
    .render(P => {
      val (s, b) = P
      val lens = State.LadderInput ^|-> inputLens(_.name)
      val nameEV = ExternalVar.state(b.$.focusStateL(lens))
      <.form(^.onSubmit ==> b.handleSubmit,
        <.label("New ladder name:", InputChanger(nameEV)),
        <.br,
        <.button("Create new ladder"))
    }).build

  val LaddersApp = ReactComponentB[Unit]("LaddersApp")
    .initialState(State(Nil, LadderInput("")))
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
          LadderInputForm((S, B)))))
    .componentDidMount(scope => {
      Ladders.fetchLadders.onSuccess {
        case ladders =>
          scope.modState(_ => State(ladders, LadderInput("")))
      }
    }).buildU

  val content = LaddersApp()
}