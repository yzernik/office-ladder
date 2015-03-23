package utils

import com.mohiva.play.silhouette.core.providers.OAuth2Settings
import com.mohiva.play.silhouette.core.providers.oauth2.GoogleProvider
import com.mohiva.play.silhouette.core.utils.CacheLayer
import com.mohiva.play.silhouette.core.utils.HTTPLayer

import play.api.Play
import play.api.Play.current

trait SocialProviderModule {

  lazy val socialProvider = GoogleProvider(cacheLayer, httpLayer, settings)

  def cacheLayer: CacheLayer
  def httpLayer: HTTPLayer

  def settings: OAuth2Settings = OAuth2Settings(
    authorizationURL = Play.configuration.getString("silhouette.google.authorizationURL").get,
    accessTokenURL = Play.configuration.getString("silhouette.google.accessTokenURL").get,
    redirectURL = Play.configuration.getString("silhouette.google.redirectURL").get,
    clientID = Play.configuration.getString("silhouette.google.clientID").get,
    clientSecret = Play.configuration.getString("silhouette.google.clientSecret").get,
    scope = Play.configuration.getString("silhouette.google.scope"))

}