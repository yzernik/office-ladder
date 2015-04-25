package officeladder

import japgolly.scalajs.react.vdom.prefix_<^._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all.div
import japgolly.scalajs.react.vdom.all.`class`

object HomePage {

  val container = ReactComponentB[Unit]("homePage")
    .render(P => {
      div(`class` := "container", content)
    }).buildU

  val content =
    <.div(
      <.h2("Office Ladder"),
      <.div(
        ^.className := "col-lg-8",
        LadderList.content))

}