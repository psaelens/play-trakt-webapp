package controllers

import play.api.mvc.Security.Authenticated
import play.api.mvc._

trait Secure {
  def username(request: RequestHeader) = request.session.get("connected")

  def onUnauthorized(request: RequestHeader) = /*render {
    case Accepts.HTML() =>*/ Results.Redirect(routes.Application.login)
    /*case Accepts.Json() => Results.Redirect(routes.Application.login)*/
  /*}*/

  def AuthenticatedAction(f: => String => Request[AnyContent] => Result) = {
    Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}