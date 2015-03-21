package models

import com.mohiva.play.silhouette.core.Identity
import com.mohiva.play.silhouette.core.LoginInfo

import play.api.libs.json.Json

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
  username: String,
  email: String) extends Identity

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

  // create the formats object for LoginInfo.
  implicit val loginInfoFormats = Json.format[LoginInfo]

  // create the formats object for LoginInfo.
  implicit val userFormats = Json.format[User]

}