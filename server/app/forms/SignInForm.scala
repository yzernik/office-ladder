package forms

import com.mohiva.play.silhouette.core.providers.Credentials

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText

/**
 * The form which handles the submission of the credentials.
 */
object SignInForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "identifier" -> nonEmptyText,
      "password" -> nonEmptyText)(Credentials.apply)(Credentials.unapply))
}