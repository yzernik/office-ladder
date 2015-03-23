package models

import java.util.UUID
import com.mohiva.play.silhouette.core.Identity
import com.mohiva.play.silhouette.core.LoginInfo
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * The user object.
 *
 * @param userID The unique ID of the user.
 * @param loginInfo The linked login info.
 * @param username The username of the authenticated user.
 * @param email The email of the authenticated provider.
 */
case class User(
  loginInfo: LoginInfo,
  firstName: String,
  lastName: String,
  fullName: String,
  email: String,
  avatarURL: Option[String]) extends Identity

/*
 * Companion object for the station case class
*/
object User {
  import play.api.libs.concurrent.Execution.Implicits._
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  /*
  def apply(providerName: String,
            providerKey: String,
            email: String,
            firstName: String,
            lastName: String,
            fullName: String,
            avatarUrl: Option[String]): User = {
    val loginInfo = LoginInfo(providerName, providerKey)
    User(loginInfo, email, firstName, lastName, fullName, avatarUrl)
  }
*/

  // create the formats object for LoginInfo.
  implicit val loginInfoFormats = Json.format[LoginInfo]

  // create the formats object for LoginInfo.
  implicit val userFormats = Json.format[User]
}

// Definition of the Users table
class Suppliers(tag: Tag) extends Table[User](tag, "users") {
  def email = column[String]("email", O.PrimaryKey) // This is the primary key column
  def firstName = column[String]("firstname")
  def lastName = column[String]("lastname")
  def fullName = column[String]("fullname")
  def avatarUrl = column[String]("avatarurl")
  def providerName = column[String]("providername")
  def providerKey = column[String]("providerkey")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (providerName, providerKey, email, firstName, lastName, fullName, avatarUrl.?) <> ((User.apply _).tupled, User.unapply)
}