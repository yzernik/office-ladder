package officeladder

import scala.scalajs.js

import org.scalajs.dom

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "Hello world!"
    HelloMessage("testing...").render(dom.document.getElementById("scalajsShoutOut"))
  }

  val vdom = <.a(
    ^.className := "google",
    ^.href := "https://www.google.com",
    <.span("GOOGLE!"))

  val HelloMessage = ReactComponentB[String]("HelloMessage")
    .render(name => <.div("Hello ", name))
    .build

}
