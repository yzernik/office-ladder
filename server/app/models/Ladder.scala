package models

import scala.slick.lifted.ProvenShape.proveShapeOf

import com.github.tototoshi.slick.JdbcJodaSupport._
import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime

case class Ladder(id: Option[Long],
                  name: String,
                  domain: String,
                  active: Boolean,
                  creator: String,
                  created: DateTime)

/*
 * Companion object for the ladder case class
*/
object Ladder {
  import play.api.libs.json.Json

  // create the formats object for Ladder.
  implicit val ladderFormats = Json.format[Ladder]
}

// Definition of the Ladders table
class Ladders(tag: Tag) extends Table[Ladder](tag, "ladders") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def domain = column[String]("domain")
  def active = column[Boolean]("active")
  def creator = column[String]("creator")
  def created = column[DateTime]("created")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, domain, active, creator, created) <> ((Ladder.apply _).tupled, Ladder.unapply _)

}