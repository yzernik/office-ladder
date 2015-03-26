package controllers

import scala.concurrent.Future

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.LoginEvent
import com.mohiva.play.silhouette.core.Silhouette
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import com.mohiva.play.silhouette.core.services.AuthInfo
import com.mohiva.play.silhouette.core.providers._

import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Request
import play.api.mvc.Result
import utils.EnvironmentModule

/**
 * The social auth controller.
 *
 * @param env The Silhouette environment.
 */
object SocialAuthController
  extends Silhouette[User, CachedCookieAuthenticator]
  with EnvironmentModule {

  /**
   * Authenticates a user against a social provider.
   *
   * @param provider The ID of the provider to authenticate against.
   * @return The result to display.
   */
  def authenticate(provider: String) = Action.async { implicit request =>
    getAuthResponse(provider)
  }

  def getAuthResponse(provider: String)(implicit request: Request[AnyContent]) = {
    val r = for {
      profile <- getSocialProfile(provider)
      resp <- getMaybeProfileResponse(profile)
    } yield resp
    r.recoverWith(exceptionHandler)
  }

  def getSocialProfile(provider: String)(implicit request: Request[AnyContent]) =
    env.providers.get(provider) match {
      case Some(p: SocialProvider[_] with CommonSocialProfileBuilder[_]) =>
        p.authenticate()
      case _ =>
        Future.failed(new AuthenticationException(s"Cannot authenticate with unexpected social provider $provider"))
    }

  def getMaybeProfileResponse[A <: AuthInfo](maybeProfile: Either[Result, CommonSocialProfile[A]])(implicit request: Request[AnyContent]) =
    maybeProfile match {
      case Left(result) =>
        Future.successful(result)
      case Right(profile: CommonSocialProfile[_]) =>
        getResponse(profile)
    }

  def getResponse[A <: AuthInfo](profile: CommonSocialProfile[A])(implicit request: Request[AnyContent]) = {
    for {
      user <- userService.save(profile)
      authInfo <- authInfoService.save(profile.loginInfo, profile.authInfo)
      maybeAuthenticator <- env.authenticatorService.create(user)
    } yield {
      maybeAuthenticator match {
        case Some(authenticator) =>
          env.eventBus.publish(LoginEvent(user, request, request2lang))
          env.authenticatorService.send(authenticator, Redirect(routes.MyApplication.index))
        case None => throw new AuthenticationException("Couldn't create an authenticator")
      }
    }
  }

}