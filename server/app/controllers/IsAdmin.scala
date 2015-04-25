package controllers

import com.mohiva.play.silhouette.api.Authorization

import models.User
import play.api.i18n.Lang
import play.api.mvc.RequestHeader

case class IsAdmin(admin: String) extends Authorization[User] {
  def isAuthorized(user: User)(implicit request: RequestHeader, lang: Lang) =
    user.email == admin
}