package controllers

import com.mohiva.play.silhouette.core.Authorization

import models.User
import play.api.i18n.Lang
import play.api.mvc.RequestHeader

case class IsAdmin(admin: String) extends Authorization[User] {
  def isAuthorized(user: User)(implicit request: RequestHeader, lang: Lang) = {
    println("checking email for authoriation: " + user.email + ", " + admin)
    user.email == admin
  }
}