package utils

import com.mohiva.play.silhouette.core.providers.CredentialsProvider
import com.mohiva.play.silhouette.core.services.AuthInfoService
import com.mohiva.play.silhouette.core.utils.PasswordHasher

trait CredentialsProviderModule {

  lazy val credentialsProvider = new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))

  def authInfoService: AuthInfoService
  def passwordHasher: PasswordHasher

}