package models.services

import scala.concurrent.Future

import models.Ladder

trait LadderService {

  /**
   * Saves a ladder.
   *
   * @param user The ladder to save.
   * @return The saved ladder.
   */
  def save(ladder: Ladder): Future[Ladder]

  /**
   * Retrieves a ladder that matches the specified id.
   *
   * @param id The id to retrieve a ladder.
   * @return The retrieved ladder or None if no ladder could be retrieved for the given id.
   */
  def retrieve(id: Long): Future[Option[Ladder]]

  /**
   * Retrieves all ladders that matche the specified domain.
   *
   * @param domain The domain to retrieve ladders.
   * @return The retrieved ladders for the given domain.
   */
  def retrieveByDomain(domain: String): Future[List[Ladder]]

}