package models

import play.api.Play.current
import anorm._
import play.api.db.DB
import controllers.TraktServiceFactory
import be.spitech.trakt.api.model._
import retrofit.RetrofitError
import play.api.Logger

case class User(username: String, password: String)

object User {

  val factory = new TraktServiceFactory("https://api.trakt.tv", "7a7dcf725280d8afcc0e508ebcc1580a")
  lazy val service = factory.create()

  val userParser: RowParser[User] = {
    import anorm.~
    import anorm.SqlParser._

    str("username") ~
      str("password") map {
        case username ~ password => User(username, password)
      }
  }

  val usersParser: ResultSetParser[List[User]] = {
    userParser *
  }

  def get(username: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(""" SELECT * FROM trakt_users
        WHERE username = {username} """).on(
        "username" -> username).as(userParser.singleOpt)
    }
  }

  def create(user: User): Boolean = {
    DB.withConnection { implicit connection =>
      val addedRows = SQL(""" INSERT INTO trakt_users
        values ({username}, {password}) """).on(
        "username" -> user.username,
        "password" -> user.password).executeUpdate()

      addedRows == 1;
    }
  }

  def delete(user: User): Boolean = {
    DB.withConnection { implicit connection =>
      val removedRows = SQL(""" DELETE FROM trakt_users
			  WHERE username = {username} """).on(
        "username" -> user.username).executeUpdate()

      removedRows > 0;
    }
  }

  def sha1(s: String) = {
    TraktServiceFactory.hash(s)
  }

  def exists(user: User): Boolean = {
    try {
      service.test(new Account(user.username, sha1(user.password)))
      delete(user)
      create(user)
    } catch {
      case error: RetrofitError => { Logger.error("Invalid username or password", error); false }
    }
  }
}