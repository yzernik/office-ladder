package models.daos

import scala.concurrent.Future

import com.mohiva.play.silhouette.core.LoginInfo

import models.Ladder

/**
 * Give access to the user object.
 */
trait LadderDAO {

  /**
   * Finds a ladder by its id.
   *
   * @param id The id info of the ladder to find.
   * @return The found ladder or None if no ladder for the given id could be found.
   */
  def find(id: Long): Option[Ladder]

  /**
   * Finds all ladders by domain.
   *
   * @param email domain The email of the ladders to find.
   * @return The found ladders for the given domain.
   */
  def find(domain: String): List[Ladder]

  /**
   * Saves a ladder.
   *
   * @param ladder The ladder to save.
   * @return The saved user.
   */
  def save(ladder: Ladder): Ladder
}