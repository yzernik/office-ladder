package models.daos

import scala.collection.mutable

import com.mohiva.play.silhouette.core.LoginInfo

import play.api.db.slick.Config.driver.simple._
import models.Users
import play.api.Play.current
import play.api.db.slick.DB

import models.Ladder
import models.Ladders

class LadderDAOImpl extends LadderDAO {

  val ladders = TableQuery[Ladders]

  def find(id: Long) = DB.withSession { implicit session =>
    ladders.filter(_.id === id).firstOption
  }

  def find(domain: String) = DB.withSession { implicit session =>
    ladders.filter(_.domain === domain).list
  }

  def save(ladder: Ladder) = DB.withSession { implicit session =>
    ladders.insert(ladder)
    ladder
  }

}