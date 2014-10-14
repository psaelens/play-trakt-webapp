package controllers

import play.api._
import play.api.mvc._
import scala.reflect.io.File
import play.api.libs.json.Json
import retrofit.RestAdapter
import be.spitech.trakt.api.client.TraktService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import play.api.data.Forms._
import play.api.data.Form
import models.User
import be.spitech.trakt.api.model.MovieLibrary
import java.util.Collections
import models.Movie
import models.kendo.FilterCondition
import scala.collection.mutable.Buffer

object Application extends Controller {

  val factory = new TraktServiceFactory("https://api.trakt.tv", "7a7dcf725280d8afcc0e508ebcc1580a")
  lazy val service = factory.create()

  def index = AuthenticatedAction { request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def login = Action {
    Ok(views.html.signin())
  }

  val signinForm = Form(
    mapping(
      "username" -> text,
      "password" -> text)(User.apply)(User.unapply))

  def signin = Action { implicit request =>
    signinForm.bindFromRequest.fold(
      withErrors => BadRequest("username and password are required"),
      user => if (User.exists(user)) {
        Ok(views.html.index("You are logged in.")).withSession(
          "connected" -> user.username)
      } else Unauthorized("Invalid username or password"))
  }

  def AuthenticatedAction(f: Request[AnyContent] => Result): Action[AnyContent] = {

    Action { request =>
      if (authenticated(request)) {
        f(request)
      } else { Unauthorized("Oops, you are not connected") }
    }
  }

  def authenticated(request: Request[AnyContent]): Boolean = {
    request.session.get("connected").nonEmpty
  }

  def read(filter: Option[models.kendo.Filter]) = AuthenticatedAction { request =>

    val username = request.session("connected")
    val result = filter match {
      case Some(filter) => filter.filters.map(_ match {
        case FilterCondition("CategoryID", _, "1") => service.collectionMovies(username)
        case FilterCondition("CategoryID", _, "2") => service.collectionMovies(username)
        case _ => service.allMovies(username)
      })
      case None => service.allMovies(username)
    }

    Ok(new GsonBuilder().create().toJson(result)).withHeaders("content-type" -> "application/json")
  }

  def read = AuthenticatedAction { request =>
    val username = request.session("connected")
    val result = request.queryString.get("filter[filters][0][value]") map { _ match {
      case Seq("1") => service.collectionMovies(username)
      case Seq("2") => service.watchedMovies(username)
      case _ => service.allMovies(username)
    } } getOrElse service.allMovies(username)

    Ok(new GsonBuilder().create().toJson(result)).withHeaders("content-type" -> "application/json")
  }

  def search = Action { request =>
    val limit = request.queryString("take")(0)
    val query = request.queryString("filter[filters][0][value]")(0)

    Ok(new GsonBuilder().create().toJson(service.search(query, Integer.valueOf(limit)))).withHeaders("content-type" -> "application/json")
  }

  val movieForm = Form(
    mapping(
      "title" -> text,
      "year" -> number,
      "imdb_id" -> text)(Movie.apply)(Movie.unapply))

  def seen = AuthenticatedAction { implicit request =>
    movieForm.bindFromRequest.fold(
      withErrors => BadRequest("Invalid movie"),
      movie => {
        val username = request.session("connected")
        User.get(username) map { user =>
          val seen = service.seen(new MovieLibrary(Collections.singletonList(new be.spitech.trakt.api.model.Movie(movie.imdb_id, movie.title, movie.year))).withAuth(user.username, user.password))
          Ok(new GsonBuilder().create().toJson(seen))
        } getOrElse (Ok(views.html.signin()).withNewSession)

      })

  }

}