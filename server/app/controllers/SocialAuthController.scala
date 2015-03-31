package controllers

import scala.concurrent.Future

import com.mohiva.play.silhouette.api.LoginEvent
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfileBuilder
import com.mohiva.play.silhouette.impl.providers.SocialProvider

import models.User
import play.api.i18n.Messages
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import utils.EnvironmentModule

/**
 * The social auth controller.
 *
 * @param env The Silhouette environment.
 */
object SocialAuthController
  extends Silhouette[User, SessionAuthenticator]
  with EnvironmentModule {

  /**
   * Authenticates a user against a social provider.
   *
   * @param provider The ID of the provider to authenticate against.
   * @return The result to display.
   */
  def authenticate(provider: String) = Action.async { implicit request =>
    (env.providers.get(provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- userService.save(profile)
            authInfo <- authInfoService.save(profile.loginInfo, authInfo)
            authenticator <- env.authenticatorService.create(profile.loginInfo)
            value <- env.authenticatorService.init(authenticator)
            result <- env.authenticatorService.embed(value, Future.successful(
              Redirect(routes.MyApplication.index())))
          } yield {
            env.eventBus.publish(LoginEvent(user, request, request2lang))
            result
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect(routes.MyApplication.signIn()).flashing("error" -> Messages("could.not.authenticate"))
    }
  }

}