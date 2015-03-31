package models.daos

import scala.collection.mutable
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info

class OAuth2InfoDAOImpl extends OAuth2InfoDAO {
  import OAuth2InfoDAOImpl._

  def save(loginInfo: LoginInfo, authInfo: OAuth2Info): Future[OAuth2Info] = {
    data += (loginInfo -> authInfo)
    Future.successful(authInfo)
  }

  def find(loginInfo: LoginInfo): Future[Option[OAuth2Info]] = {
    Future.successful(data.get(loginInfo))
  }

}

/**
 * The companion object.
 */
object OAuth2InfoDAOImpl {

  /**
   * The data store for the OAuth2 info.
   */
  var data: mutable.HashMap[LoginInfo, OAuth2Info] = mutable.HashMap()
}