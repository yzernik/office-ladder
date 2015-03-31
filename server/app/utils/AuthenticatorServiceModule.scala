package utils

import com.mohiva.play.silhouette.api.services._
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.authenticators._

import play.api.Play
import play.api.Play.current

trait AuthenticatorServiceModule {

  lazy val authenticatorService: AuthenticatorService[SessionAuthenticator] = {
    new SessionAuthenticatorService(SessionAuthenticatorSettings(
      sessionKey = Play.configuration.getString("silhouette.authenticator.sessionKey").get,
      encryptAuthenticator = Play.configuration.getBoolean("silhouette.authenticator.encryptAuthenticator").get,
      useFingerprinting = Play.configuration.getBoolean("silhouette.authenticator.useFingerprinting").get,
      authenticatorIdleTimeout = Play.configuration.getInt("silhouette.authenticator.authenticatorIdleTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").get), fingerprintGenerator, Clock())
  }

  def cacheLayer: CacheLayer
  def idGenerator: IDGenerator
  def fingerprintGenerator: FingerprintGenerator

}