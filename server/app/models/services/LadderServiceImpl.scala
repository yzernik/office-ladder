package models.services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import models.Ladder
import models.daos.LadderDAO

class LadderServiceImpl(ladderDAO: LadderDAO) extends LadderService {

  import scala.concurrent.ExecutionContext.Implicits.global

  def save(ladder: Ladder): Future[Ladder] =
    Future { ladderDAO.save(ladder) }

  def retrieve(id: Long): Future[Option[Ladder]] =
    Future { ladderDAO.find(id) }

  def retrieveByDomain(domain: String, onlyActive: Boolean = true): Future[List[Ladder]] =
    Future { ladderDAO.find(domain, onlyActive) }

}