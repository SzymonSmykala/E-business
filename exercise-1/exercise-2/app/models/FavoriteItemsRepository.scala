package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class FavoriteItemsRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import userRepository.UserTable;
  import productRepository.ProductTable;

  private val user = TableQuery[UserTable]
  private val product = TableQuery[ProductTable]

  private val favoriteItem = TableQuery[FavoriteItemTable]
  private class FavoriteItemTable(tag: Tag) extends Table[FavoriteItem](tag, "favorite_item") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def user = column[Long]("user_id")
    def product = column[Long]("product_id")
    def * = (id, user, product) <> ((FavoriteItem.apply _).tupled, FavoriteItem.unapply)
  }

  def create(id: Long, userId : Long, productId: Long): Future[FavoriteItem] = db.run {
    (favoriteItem.map(c => (c.user, c.product))
      returning favoriteItem.map(_.id)
      into {case ((user_id, product_id), id) => FavoriteItem(id, user_id, product_id)}
      ) += (userId, productId)
  }

  def list(): Future[Seq[FavoriteItem]] = db.run {
    favoriteItem.result
  }

  def getById(id: Long): Future[FavoriteItem] = db.run{
    favoriteItem.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    favoriteItem.filter(_.id === id).delete
  }

  def getFavoriteItemsByUserId(userId: Long) = db.run {
    favoriteItem.filter(_.user === userId).result.head
  }

}
