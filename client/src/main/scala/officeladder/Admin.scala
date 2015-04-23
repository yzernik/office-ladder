package officeladder

import japgolly.scalajs.react.vdom.prefix_<^._

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all.div
import japgolly.scalajs.react.vdom.all.`class`

object Admin {

  val container = ReactComponentB[Unit]("adminPage")
    .render(P => {
      div(`class` := "container", content)
    }).buildU

  val content =
    <.div(
      <.h2("Admin Page"),
      <.div(
        ^.className := "col-lg-8",
        Ladders.content))

}