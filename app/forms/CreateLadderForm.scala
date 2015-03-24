package forms

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText

/**
 * The form which handles the sign up process.
 */
object CreateLadderForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "domain" -> nonEmptyText)(LadderData.apply)(LadderData.unapply))

  /**
   * The ladder form data.
   * Partial data for creating a new ladder.
   *
   * @param username The username of a user.
   * @param email The email of the user.
   * @param password The password of the user.
   */
  case class LadderData(
    name: String,
    domain: String)

}

