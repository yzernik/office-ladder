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
      "name" -> nonEmptyText)(LadderData.apply)(LadderData.unapply))

  /**
   * The ladder form data.
   * Partial data for creating a new ladder.
   *
   * @param name The name of the new ladder.
   * @param domain The email domain of the new ladder.
   */
  case class LadderData(
    name: String)

}

