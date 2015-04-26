package models.daos

import models.Ladder

/**
 * Give access to the user object.
 */
trait LadderDAO {

  /**
   * Finds all ladders.
   *
   * @return The found ladders.
   */
  def findAll(): List[Ladder]

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
  def find(domain: String, onlyActive: Boolean): List[Ladder]

  /**
   * Saves a ladder.
   *
   * @param ladder The ladder to save.
   * @return The saved user.
   */
  def save(ladder: Ladder): Ladder

  /**
   * Updates the active status of a ladder.
   *
   * @param id of the ladder to update, and new active status.
   * @return unit.
   */
  def updateActiveStatus(id: Long, active: Boolean): Unit
}