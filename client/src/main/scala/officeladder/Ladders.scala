package officeladder

import scala.concurrent.Future
import scala.util.{ Success, Failure }
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import org.scalajs.dom.ext._

import monocle.macros._
import upickle._

import models._

object Ladders {

  val baseUrl = org.scalajs.dom.window.location.origin
  val laddersUrl = baseUrl + "/ladders"
  val adminUrl = baseUrl + "/admin"

  val contentTypeJson = "Content-Type" -> "application/json"

  def fetchLadders: Future[List[Ladder]] = {
    Ajax.get(laddersUrl).map { res =>
      read[List[Ladder]](res.responseText)
    }
  }

  def createLadder(ldr: LadderInput): Future[Ladder] = {
    val data = write[LadderInput](ldr)
    Ajax.post(laddersUrl, data, headers = Map(contentTypeJson)).map { res =>
      read[Ladder](res.responseText)
    }
  }

  def fetchAdminLadders: Future[List[Ladder]] = {
    val url = adminUrl + "/ladders"
    Ajax.get(url).map { res =>
      read[List[Ladder]](res.responseText)
    }
  }

  def activateLadder(id: Long): Future[Ladder] = {
    val url = adminUrl + "/ladders" + "/" + id
    Ajax.post(url).map { res =>
      read[Ladder](res.responseText)
    }
  }

}