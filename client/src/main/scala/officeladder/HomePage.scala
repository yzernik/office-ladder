package officeladder

import japgolly.scalajs.react.vdom.prefix_<^._

object HomePage {

  val content =
    <.div(
      <.h2("Office Ladder"),
      <.div(
        ^.className := "col-lg-8",
        Ladders.content))

}