package controllers

import scala.concurrent.Future

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.LogoutEvent
import com.mohiva.play.silhouette.core.Silhouette

import forms.SignInForm
import forms.SignUpForm
import models.User
import utils.EnvironmentModule

object MyApplication
  extends Silhouette[User, CachedCookieAuthenticator]
  with EnvironmentModule {

  val adminEmail = play.Play.application.configuration.getString("admin.email")

  /**
   * Renders the index page.
   *
   * @returns The result to send to the client.
   */
  def index = UserAwareAction { implicit request =>
    val userName = request.identity match {
      case Some(identity) => identity.fullName
      case None           => "Guest"
    }
    Ok(views.html.index(request.identity, "Hello %s".format(userName)))
  }

  /**
   * Handles the Sign In action.
   *
   * @return The result to display.
   */
  def signIn = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.MyApplication.index))
      case None       => Future.successful(Ok(views.html.signIn(SignInForm.form)))
    }
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))
    Future.successful(env.authenticatorService.discard(Redirect(routes.MyApplication.index)))
  }

  /**
   * Handles the admin page action.
   *
   * @return The result to display.
   */
  def adminPage = SecuredAction(IsAdmin(adminEmail)) { implicit request =>
    Ok("Welcome to the admin page.")
  }

}