package models

import scala.slick.lifted.ProvenShape.proveShapeOf

import com.mohiva.play.silhouette.api._
import org.joda.time.DateTime
import com.github.tototoshi.slick.JdbcJodaSupport._

import play.api.db.slick.Config.driver.simple._

/**
 * The user object.
 *
 */
case class User(
  loginInfo: LoginInfo,
  firstName: String,
  lastName: String,
  fullName: String,
  email: String,
  created: DateTime) extends Identity

/*
 * Companion object for the user case class
*/
object User {
  import play.api.libs.json.Json

  // create the formats object for LoginInfo.
  implicit val loginInfoFormats = Json.format[LoginInfo]

  // create the formats object for LoginInfo.
  implicit val userFormats = Json.format[User]
}

// Definition of the Users table
class Users(tag: Tag) extends Table[User](tag, "users") {

  def email = column[String]("email", O.PrimaryKey) // This is the primary key column
  def providerName = column[String]("providername")
  def providerKey = column[String]("providerkey")
  def firstName = column[String]("firstname")
  def lastName = column[String]("lastname")
  def fullName = column[String]("fullname")
  def created = column[DateTime]("created")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (email, providerName, providerKey, firstName, lastName, fullName, created) <> ((mapRow _).tupled, unMapRow _)

  private def mapRow(
    email: String,
    providerName: String,
    providerKey: String,
    firstName: String,
    lastName: String,
    fullName: String,
    created: DateTime): User = {
    val loginInfo = LoginInfo(providerName, providerKey)
    User(loginInfo, firstName, lastName, fullName, email, created)
  }

  private def unMapRow(user: User) = {
    val providerName = user.loginInfo.providerID
    val providerKey = user.loginInfo.providerKey
    val firstName = user.firstName
    val lastName = user.lastName
    val fullName = user.fullName
    val email = user.email
    val created = user.created

    val tuple = (email, providerName, providerKey, firstName, lastName, fullName, created)
    Some(tuple)
  }

}