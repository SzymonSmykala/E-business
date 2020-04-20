package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class BasketRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import userRepository.UserTable
  private val userTable = TableQuery[UserTable]

  private val basket = TableQuery[BasketTable]
  class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def user = column[Long]("user_id")
    private def user_fk = foreignKey("userTable_fk",user, userTable)(_.id)
    def * = (id, user) <> ((Basket.apply _).tupled, Basket.unapply)
  }

  def create(id: Long, user_id: Long): Future[Basket] = db.run {
    (basket.map(c => (c.user))
      returning basket.map(_.id)
      into {case ((user), id) => Basket(id, user)}
      ) += (user_id)
  }

  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }

  def getById(id: Long): Future[Basket] = db.run{
    basket.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    basket.filter(_.id === id).delete
  }

  def getBasketByUserId(user_id: Long): Future[Basket] = db.run {
    basket.filter(_.user === user_id).result.head
  }

}
