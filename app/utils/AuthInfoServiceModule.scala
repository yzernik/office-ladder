package utils

import com.mohiva.play.silhouette.contrib.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.contrib.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.core.providers.OAuth1Info
import com.mohiva.play.silhouette.core.providers.OAuth2Info
import com.mohiva.play.silhouette.core.providers.PasswordInfo

trait AuthInfoServiceModule {

  lazy val authInfoService = new DelegableAuthInfoService(passwordInfoDAO)

  def passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]

}