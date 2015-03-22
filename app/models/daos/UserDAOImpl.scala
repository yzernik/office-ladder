package models.daos

import scala.collection.mutable
import scala.concurrent.Future

import com.mohiva.play.silhouette.core.LoginInfo

import models.User
import models.services.UserCreationException
import UserDAOImpl._

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = Future.successful(
    users.find { case (id, user) => user.loginInfo == loginInfo }.map(_._2))

  /**
   * Finds a user by its username.
   *
   * @param username The username of the user to find.
   * @return The found user or None if no user for the given username could be found.
   */
  def find(username: String) = Future.successful(users.get(username))

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    if (users.contains(user.email)) {
      Future.failed(new UserCreationException("user with that email already exists."))
    } else {
      users += (user.email -> user)
      Future.successful(user)
    }
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {

  /**
   * The list of users.
   */
  val users: mutable.HashMap[String, User] = mutable.HashMap()
}