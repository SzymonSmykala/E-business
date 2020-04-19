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
  private val prod = TableQuery[UserTable]

  private val basket = TableQuery[BasketTable]
  private class BasketTable(tag: Tag) extends Table[ProductDescription](tag, "basket") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("user_id")
    def description = column[String]("description")
    def product_fk = foreignKey("prod_fk",product, prod)(_.id)
    def * = (id, product, description) <> ((ProductDescription.apply _).tupled, ProductDescription.unapply)
  }

//  def create(id: Long, product_id: Long, description: String): Future[ProductDescription] = db.run {
//    (productDescription.map(c => (c.product, c.description))
//      returning productDescription.map(_.id)
//      into {case ((product, description), id) => ProductDescription(id, product, description)}
//      ) += (product_id, description)
//  }
//
//  def list(): Future[Seq[ProductDescription]] = db.run {
//    productDescription.result
//  }
//
//  def getById(id: Long): Future[ProductDescription] = db.run{
//    productDescription.filter(_.id === id).result.head
//  }
//
//  def delete(id: Long): Future[Int] = db.run {
//    productDescription.filter(_.id === id).delete
//  }
//
//  def update(id: Long, newProductDescription: ProductDescription): Future[Unit] = {
//    val productToUpdate: ProductDescription = newProductDescription.copy(id, newProductDescription.productId, newProductDescription.description)
//    db.run {
//      productDescription.filter(_.id === id).update(productToUpdate).map(_ => ())
//    }
//  }

}
