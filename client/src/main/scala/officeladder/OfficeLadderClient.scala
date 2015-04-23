package officeladder

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object OfficeLadderClient extends js.JSApp {

  val container = ReactComponentB[Unit]("homePage")
    .render(P => {
      div(`class` := "container",
        HomePage.content)
    }).buildU

  def main(): Unit = {
    // do nothing!
  }

  def renderClient[P, S, B, N <: TopNode](c: ReactComponentU[P, S, B, N]) = {
    c.render(dom.document.getElementById("client"))
  }

  @JSExport
  def renderHome(): Unit =
    renderClient(container())

}
