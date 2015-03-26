package utils

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.contrib.utils.BCryptPasswordHasher
import com.mohiva.play.silhouette.contrib.utils.PlayCacheLayer
import com.mohiva.play.silhouette.contrib.utils.SecureRandomIDGenerator
import com.mohiva.play.silhouette.core.Environment
import com.mohiva.play.silhouette.core.EventBus
import com.mohiva.play.silhouette.core.utils.PlayHTTPLayer

import models.User
import models.daos.OAuth2InfoDAOImpl

trait EnvironmentModule
  extends AuthenticatorServiceModule
  with UserServiceModule
  with AuthInfoServiceModule
  with SocialProviderModule
  with LadderServiceModule {

  lazy val cacheLayer = new PlayCacheLayer
  lazy val httpLayer = new PlayHTTPLayer
  lazy val eventBus = EventBus()
  lazy val idGenerator = new SecureRandomIDGenerator
  lazy val authInfoDAO = new OAuth2InfoDAOImpl
  lazy val passwordHasher = new BCryptPasswordHasher
  implicit lazy val env: Environment[User, CachedCookieAuthenticator] = {
    Environment[User, CachedCookieAuthenticator](
      userService,
      authenticatorService,
      Map(
        "google" -> socialProvider),
      eventBus)
  }

}