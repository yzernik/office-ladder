package utils

import com.mohiva.play.silhouette.contrib.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.contrib.services.DelegableAuthInfoService
import com.mohiva.play.silhouette.core.providers.OAuth2Info

trait AuthInfoServiceModule {

  lazy val authInfoService = new DelegableAuthInfoService(authInfoDAO)

  def authInfoDAO: DelegableAuthInfoDAO[OAuth2Info]

}