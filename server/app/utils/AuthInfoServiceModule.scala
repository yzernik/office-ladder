package utils

import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService

trait AuthInfoServiceModule {

  lazy val authInfoService = new DelegableAuthInfoService(authInfoDAO)

  def authInfoDAO: DelegableAuthInfoDAO[OAuth2Info]

}