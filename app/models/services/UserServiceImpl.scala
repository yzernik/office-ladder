package models.services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.joda.time.DateTime

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import com.mohiva.play.silhouette.core.services.AuthInfo

import models.User
import models.daos.UserDAO

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl(userDAO: UserDAO) extends UserService {

  import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve a user.
   * @return The retrieved user or None if no user could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] =
    Future { userDAO.find(loginInfo) }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) =
    Future { userDAO.save(user) }

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save[A <: AuthInfo](profile: CommonSocialProfile[A]): Future[User] =
    userDAO.find(profile.loginInfo) match {
      case Some(user) => // Update user with profile
        save(user.copy(
          firstName = profile.firstName.get,
          lastName = profile.lastName.get,
          fullName = profile.fullName.get,
          email = profile.email.get))
      case None => // Insert a new user
        save(User(
          loginInfo = profile.loginInfo,
          created = DateTime.now,
          firstName = profile.firstName.get,
          lastName = profile.lastName.get,
          fullName = profile.fullName.get,
          email = profile.email.get))
    }

}