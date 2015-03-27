package officeladder

import scala.scalajs.js

import org.scalajs.dom

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object ScalaJSExample extends js.JSApp {

  val container = ReactComponentB[Unit]("homePage")
    .render(P => {
      div(`class` := "container",
        HomePage.content)
    }).buildU

  def main(): Unit = {
    container().render(dom.document.getElementById("scalajsShoutOut"))
  }
}
