package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private var user = TableQuery[UserTable]
  private class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")
    def password = column[String]("password")
    def * = (id, email, password) <> ((User.apply _).tupled, User.unapply)
  }

  def create(id: Long, email: String, password: String): Future[User] = db.run {
    (user.map(c => (c.email, c.password))
      returning user.map(_.id)
      into {case ((email, password), id) => User(id, email, password)}
      ) += (email, password)
  }

  def getById(id: Long): Future[User] = db.run{
    user.filter(_.id === id).result.head
  }

  def getByEmail(email: String ) : Future[User] = db.run{
    user.filter(_.email === email).result.head
  }
}
