package utils

import models.daos.LadderDAOImpl
import models.services.LadderServiceImpl

trait LadderServiceModule {

  lazy val ladderDAO = new LadderDAOImpl
  lazy val ladderService = new LadderServiceImpl(ladderDAO)

}