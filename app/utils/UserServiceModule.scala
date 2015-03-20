package utils

import models.daos.UserDAOImpl
import models.services.UserServiceImpl

trait UserServiceModule {

  lazy val userDAO = new UserDAOImpl
  lazy val userService = new UserServiceImpl(userDAO)

}