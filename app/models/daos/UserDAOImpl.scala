package models.daos

import scala.collection.mutable

import com.mohiva.play.silhouette.core.LoginInfo

import play.api.db.slick.Config.driver.simple._
import models.Users
import play.api.Play.current
import play.api.db.slick.DB

import models.User

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {
  import UserDAOImpl._

  val users = TableQuery[Users]

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = DB.withSession { implicit session =>
    users.filter(u => (u.providerName === loginInfo.providerID &&
      u.providerKey === loginInfo.providerKey)).firstOption
  }

  /**
   * Finds a user by its username.
   *
   * @param username The username of the user to find.
   * @return The found user or None if no user for the given username could be found.
   */
  def find(email: String) = DB.withSession { implicit session =>
    users.filter(_.email === email).firstOption
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = DB.withSession { implicit session =>
    users.update(user)
    user
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {

  /**
   * The list of users.
   */
  //val users: mutable.HashMap[String, User] = mutable.HashMap()
}