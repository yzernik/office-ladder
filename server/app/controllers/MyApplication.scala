package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.joda.time.DateTime
import com.mohiva.play.silhouette.api.LogoutEvent
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms.CreateLadderForm
import models.Ladder
import models.Ladder.ladderFormats
import models.User
import play.api.libs.json.Json
import utils.EnvironmentModule
import play.api.mvc.ActionTransformer
import play.api.mvc.Request
import com.mohiva.play.silhouette.api.Identity
import play.api.mvc.WrappedRequest

object MyApplication
  extends Silhouette[User, SessionAuthenticator]
  with EnvironmentModule {

  import scala.concurrent.ExecutionContext.Implicits.global

  val adminEmail = play.Play.application.configuration.getString("admin.email")

  /**
   * Renders the index page.
   *
   * @returns The result to send to the client.
   */
  def index = UserAwareAction { implicit request =>
    val userName = request.identity match {
      case Some(identity) => identity.fullName
      case None           => "Guest"
    }
    Ok(views.html.index(request.identity))
  }

  /**
   * Handles the Sign In action.
   *
   * @return The result to display.
   */
  def signIn = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.MyApplication.index))
      case None       => Future.successful(Ok(views.html.signIn()))
    }
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request =>
    val result = Future.successful(Redirect(routes.MyApplication.index()))
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))

    request.authenticator.discard(result)
  }

  /**
   * Handles the admin page action.
   *
   * @return The result to display.
   */
  def adminPage = SecuredAction(IsAdmin(adminEmail)) { implicit request =>
    Ok("Welcome to the admin page.")
  }

  def ladders = SecuredAction.async { implicit request =>
    val domain = request.identity.email.split('@')(1)
    ladderService.retrieveByDomain(domain).map {
      ldrs => Ok(Json.toJson(ldrs))
    }
  }

  /**
   * Handles the create ladder action.
   *
   * @return The result to display.
   */
  def createLadder = SecuredAction(IsAdmin(adminEmail)).async { implicit request =>
    CreateLadderForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest("bad input data.")),
      data => {
        val userEmail = request.identity.email
        val ladder = new Ladder(None, data.name, data.domain, userEmail, DateTime.now)
        ladderService.save(ladder).map { res => Ok }
      })
  }

  object FetchLadders extends ActionTransformer[SecuredRequest, LaddersRequest] {
    def transform[A](request: SecuredRequest[A]) = {
      val id = request.identity
      val domain = request.identity.email.split('@')(1)
      ladderService.retrieveByDomain(domain).map { ldrs =>
        LaddersRequest(id, ldrs, request)
      }
    }
  }

  case class LaddersRequest[A](
    val user: Identity,
    val notifications: List[Ladder],
    request: Request[A]) extends WrappedRequest(request)

}