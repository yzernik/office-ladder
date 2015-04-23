package officeladder

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object OfficeLadderClient extends js.JSApp {

  def main(): Unit = {
    // do nothing!
  }

  private def renderClient[P, S, B, N <: TopNode](c: ReactComponentU[P, S, B, N]) = {
    c.render(dom.document.getElementById("client"))
  }

  @JSExport
  def renderHome(): Unit =
    renderClient(HomePage.container())

  @JSExport
  def renderAdmin(): Unit =
    renderClient(Admin.container())

}
