package models.daos

import models.Ladder
import models.Ladders
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

class LadderDAOImpl extends LadderDAO {

  val ladders = TableQuery[Ladders]

  def find(id: Long) = DB.withSession { implicit session =>
    ladders.filter(_.id === id).firstOption
  }

  def find(domain: String, onlyActive: Boolean) = DB.withSession { implicit session =>
    ladders.filter(ldrs =>
      ldrs.domain === domain &&
        (if (onlyActive) ldrs.active else true)).list
  }

  def save(ladder: Ladder) = DB.withSession { implicit session =>
    val newId = (ladders returning ladders.map(_.id)).insert(ladder)
    ladder.copy(id = Some(newId))
  }

}