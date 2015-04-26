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

import models.Ladder.ladderFormats

object MyApplication
  extends Silhouette[User, SessionAuthenticator]
  with EnvironmentModule {

  import scala.concurrent.ExecutionContext.Implicits.global

  val adminEmail = play.Play.application.configuration.getString("admin.email")
  val adminFilter = IsAdmin(adminEmail)

  /**
   * Renders the index page.
   *
   * @returns The result to send to the client.
   */
  def index = UserAwareAction { implicit request =>
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
   * Returns the list of ladders for the user's domain.
   */
  def ladders = SecuredAction.async { implicit request =>
    val domain = request.identity.domain
    ladderService.retrieveByDomain(domain).map {
      ldrs => Ok(Json.toJson(ldrs))
    }
  }

  /**
   * Handles the create ladder action.
   *
   * @return The result to display.
   */
  def createLadder = SecuredAction.async { implicit request =>
    CreateLadderForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest("bad input data.")),
      data => {
        val userEmail = request.identity.email
        val domain = request.identity.domain
        val ladder = new Ladder(None, data.name, domain, false, userEmail, DateTime.now)
        ladderService.save(ladder).map { ldr =>
          Ok(Json.toJson(ldr))
        }
      })
  }

  /**
   * Handles the admin page action.
   *
   * @return The result to display.
   */
  def adminPage = SecuredAction(adminFilter) { implicit request =>
    Ok(views.html.admin(request.identity))
  }

  /**
   * Returns the list of ladders for the user's domain.
   */
  def adminLadders = SecuredAction(adminFilter).async { implicit request =>
    val domain = request.identity.domain
    ladderService.retrieveAll.map {
      ldrs => Ok(Json.toJson(ldrs))
    }
  }

  /**
   * Handles the activate ladder action.
   *
   */
  def activateLadder(ladderId: Long) = SecuredAction(adminFilter).async {
    ladderService.updateActiveStatus(ladderId, true).map { ldr =>
      Ok(s"Activated ladder: ${ladderId}")
    }
  }

  object FetchLadders extends ActionTransformer[SecuredRequest, LaddersRequest] {
    def transform[A](request: SecuredRequest[A]) = {
      val id = request.identity
      val domain = id.domain
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