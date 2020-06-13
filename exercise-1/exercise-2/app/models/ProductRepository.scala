package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext, categoryRepository: CategoryRepository) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import categoryRepository.CategoryTable


  class ProductTable(tag: Tag) extends Table[Product](tag, "product") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def category = column[Long]("category_id")
    def name = column[String]("name")
    def price = column[Int]("price")
    def * = (name, category, id, price) <> ((Product.apply _).tupled, Product.unapply)
  }

  val product = TableQuery[ProductTable]
  private val categoryTable = TableQuery[CategoryTable]

  def create(name: String, categoryId: Long, id: Long, price: Int): Future[Product] = db.run {
    (product.map(c => (c.name, c.category, c.price))
      returning product.map(_.id)
      into {case ((name, categoryId, price), id) => Product(name, categoryId,id, price)}
      ) += (name, categoryId,price)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }

  def getById(id: Long): Future[Product] = db.run{
    product.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    product.filter(_.id === id).delete
  }

  def update(id: Long, newProduct: Product): Future[Unit] = {
    val productToUpdate: Product = newProduct.copy(newProduct.name, newProduct.categoryId, id)
    db.run {
      product.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}

