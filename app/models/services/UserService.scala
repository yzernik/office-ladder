package models.services

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import com.mohiva.play.silhouette.core.services.AuthInfo
import com.mohiva.play.silhouette.core.services.IdentityService

import models.User

/**
 * Handles actions to users.
 */
trait UserService extends IdentityService[User] {

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User)(implicit ec: ExecutionContext): Future[User]

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save[A <: AuthInfo](profile: CommonSocialProfile[A])(implicit ec: ExecutionContext): Future[User]

}

/**
 * An exception thrown when the user cannot be created.
 *
 * @param msg The exception message.
 * @param cause The exception cause.
 */
case class UserCreationException(msg: String, cause: Throwable)
  extends Exception(msg, cause) {

  //logger.error(msg, cause)

  /**
   * Constructs an exception with only a message.
   *
   * @param msg The exception message.
   */
  def this(msg: String) = this(msg, null)
}