package officeladder

import japgolly.scalajs.react._, vdom.prefix_<^._
import scalajs.js

import org.scalajs.jquery.jQuery

import models._

object Ladders {

  case class State(ladders: List[Ladder])

  class Backend($: BackendScope[Unit, State]) {
    // add some methods..

    def fetchLadders() =

      $.modState(s => State(Nil))
  }

  val LaddersList = ReactComponentB[List[Ladder]]("LaddersList")
    .render(props => {
      def createLadderItem(ladder: Ladder) = <.li(ladder.name)
      <.ul(props map createLadderItem)
    })
    .build

  val LaddersApp = ReactComponentB[Unit]("TodoApp")
    .initialState(State(Nil))
    .backend(new Backend(_))
    .render((_, S, B) =>
      <.div(
        <.h3("Ladders"),
        LaddersList(S.ladders)))
    .componentDidMount(scope => {
      // make ajax call here to get pics from instagram
      import scalajs.js.Dynamic.{ global => g }
      val url = "https://localhost:9000/ladders"

      val fn = (result: js.Dynamic) => {
        if (result != js.undefined && result.data != js.undefined) {
          val data = result.data.asInstanceOf[js.Array[js.Dynamic]]
          val ladders = data.toList.map(item => Ladder(item.id.toString.toLong, item.name.toString, item.domain.toString, item.creator.toString, item.created.toString.toLong))
          scope.modState(_ => State(ladders))
        }
      }

      jQuery.get(url, Nil, fn, Nil)

      /*
      g.jsonp(url, (result: js.Dynamic) => {
        if (result != js.undefined && result.data != js.undefined) {
          val data = result.data.asInstanceOf[js.Array[js.Dynamic]]
          val ladders = data.toList.map(item => Ladder(item.id.toString.toLong, item.name.toString, item.domain.toString, item.creator.toString, item.created.toString.toLong))
          scope.modState(_ => State(ladders))
        }
      })
      * 
      */
    }).buildU

  val content = LaddersApp()
}