package utils

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.authenticators._
import com.mohiva.play.silhouette.impl.util._

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
  lazy val fingerprintGenerator = new DefaultFingerprintGenerator(false)
  lazy val iDGenerator = new SecureRandomIDGenerator()
  implicit lazy val env: Environment[User, SessionAuthenticator] = {
    Environment[User, SessionAuthenticator](
      userService,
      authenticatorService,
      Map(
        "google" -> socialProvider),
      eventBus)
  }

}