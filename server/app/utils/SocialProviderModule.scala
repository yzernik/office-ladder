package utils

import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.providers.oauth2._
import com.mohiva.play.silhouette.impl.providers.oauth2.state._

import play.api.Play
import play.api.Play.current

trait SocialProviderModule {

  lazy val socialProvider = GoogleProvider(httpLayer, oAuth2StateProvider, settings)

  def cacheLayer: CacheLayer
  def httpLayer: HTTPLayer
  def idGenerator: IDGenerator

  def settings: OAuth2Settings = OAuth2Settings(
    authorizationURL = Play.configuration.getString("silhouette.google.authorizationURL"),
    accessTokenURL = Play.configuration.getString("silhouette.google.accessTokenURL").get,
    redirectURL = Play.configuration.getString("silhouette.google.redirectURL").get,
    clientID = Play.configuration.getString("silhouette.google.clientID").get,
    clientSecret = Play.configuration.getString("silhouette.google.clientSecret").get,
    scope = Play.configuration.getString("silhouette.google.scope"))

  def oAuth2StateProvider = {
    new CookieStateProvider(CookieStateSettings(
      cookieName = Play.configuration.getString("silhouette.oauth2StateProvider.cookieName").get,
      cookiePath = Play.configuration.getString("silhouette.oauth2StateProvider.cookiePath").get,
      cookieDomain = Play.configuration.getString("silhouette.oauth2StateProvider.cookieDomain"),
      secureCookie = Play.configuration.getBoolean("silhouette.oauth2StateProvider.secureCookie").get,
      httpOnlyCookie = Play.configuration.getBoolean("silhouette.oauth2StateProvider.httpOnlyCookie").get,
      expirationTime = Play.configuration.getInt("silhouette.oauth2StateProvider.expirationTime").get), idGenerator, Clock())
  }

}