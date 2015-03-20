package controllers

import scala.concurrent.Future

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.LoginEvent
import com.mohiva.play.silhouette.core.Silhouette
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.core.providers.CredentialsProvider

import forms.SignInForm
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import utils.EnvironmentModule

/**
 * The credentials auth controller.
 *
 * @param env The Silhouette environment.
 */
object CredentialsAuthController
  extends Silhouette[User, CachedCookieAuthenticator]
  with EnvironmentModule {

  /**
   * Authenticates a user against the credentials provider.
   *
   * @return The result to display.
   */
  def authenticate = Action.async { implicit request =>
    SignInForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signIn(form))),
      credentials => (env.providers.get(CredentialsProvider.Credentials) match {
        case Some(p: CredentialsProvider) => p.authenticate(credentials)
        case _ => Future.failed(new AuthenticationException(s"Cannot find credentials provider"))
      }).flatMap { loginInfo =>
        userService.retrieve(loginInfo).flatMap {
          case Some(user) => env.authenticatorService.create(user).map {
            case Some(authenticator) =>
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              env.authenticatorService.send(authenticator, Redirect(routes.MyApplication.index))
            case None => throw new AuthenticationException("Couldn't create an authenticator")
          }
          case None => Future.failed(new AuthenticationException("Couldn't find user"))
        }
      }.recoverWith(exceptionHandler))
  }

}