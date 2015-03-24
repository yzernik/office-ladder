package models.daos

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

  val users = TableQuery[Users]

  def find(loginInfo: LoginInfo) = DB.withSession { implicit session =>
    users.filter(u => (u.providerName === loginInfo.providerID &&
      u.providerKey === loginInfo.providerKey)).firstOption
  }

  def find(email: String) = DB.withSession { implicit session =>
    users.filter(_.email === email).firstOption
  }

  def save(user: User) = DB.withSession { implicit session =>
    if (find(user.email).isEmpty) {
      users.insert(user)
    }
    user
  }
}