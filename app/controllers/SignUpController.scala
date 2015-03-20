package controllers

import scala.concurrent.Future

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.LoginEvent
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.SignUpEvent
import com.mohiva.play.silhouette.core.Silhouette
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.core.providers.CredentialsProvider

import forms.SignUpForm
import models.User
import models.services.UserCreationException
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import utils.EnvironmentModule

/**
 * The sign up controller.
 *
 * @param env The Silhouette environment.
 * @param userService The user service implementation.
 * @param authInfoService The auth info service implementation.
 * @param avatarService The avatar service implementation.
 * @param passwordHasher The password hasher implementation.
 */
object SignUpController
  extends Silhouette[User, CachedCookieAuthenticator]
  with EnvironmentModule {

  /**
   * Registers a new user.
   *
   * @return The result to display.
   */
  def signUp = Action.async { implicit request =>
    SignUpForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signUp(form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.Credentials, data.username)
        val authInfo = passwordHasher.hash(data.password)
        val user = User(
          loginInfo = loginInfo,
          username = data.username,
          email = data.email)
        val result = for {
          user <- userService.save(user)
          authInfo <- authInfoService.save(loginInfo, authInfo)
          maybeAuthenticator <- env.authenticatorService.create(user)
        } yield {
          maybeAuthenticator match {
            case Some(authenticator) =>
              env.eventBus.publish(SignUpEvent(user, request, request2lang))
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              env.authenticatorService.send(authenticator, Redirect(routes.MyApplication.index))
            case None => throw new AuthenticationException("Couldn't create an authenticator")
          }
        }
        result.recover {
          case UserCreationException(msg, t) => Forbidden(msg)
        }
      })
  }
}

