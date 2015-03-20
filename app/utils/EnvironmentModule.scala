package utils

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.contrib.utils.BCryptPasswordHasher
import com.mohiva.play.silhouette.contrib.utils.PlayCacheLayer
import com.mohiva.play.silhouette.contrib.utils.SecureRandomIDGenerator
import com.mohiva.play.silhouette.core.Environment
import com.mohiva.play.silhouette.core.EventBus

import models.User
import models.daos.PasswordInfoDAO
import models.services.UserService

trait EnvironmentModule
  extends AuthenticatorServiceModule
  with UserServiceModule
  with AuthInfoServiceModule
  with CredentialsProviderModule {

  lazy val cacheLayer = new PlayCacheLayer
  lazy val eventBus = EventBus()
  lazy val idGenerator = new SecureRandomIDGenerator
  lazy val passwordInfoDAO = new PasswordInfoDAO
  lazy val passwordHasher = new BCryptPasswordHasher
  implicit lazy val env: Environment[User, CachedCookieAuthenticator] = {
    Environment[User, CachedCookieAuthenticator](
      userService,
      authenticatorService,
      Map(
        credentialsProvider.id -> credentialsProvider),
      eventBus)
  }

}