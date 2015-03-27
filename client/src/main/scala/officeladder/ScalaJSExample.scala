package officeladder

import scala.scalajs.js

import org.scalajs.dom

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    ProductTableExample.content.render(dom.document.getElementById("scalajsShoutOut"))
  }
}
