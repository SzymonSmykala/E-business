package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProductDescriptionRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import productRepository.ProductTable
  private val prod = TableQuery[ProductTable]

  private val productDescription = TableQuery[ProductDescriptionTable]
  private class ProductDescriptionTable(tag: Tag) extends Table[ProductDescription](tag, "product_description") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product_id")
    def description = column[String]("description")
    def productFk = foreignKey("prod_fk",product, prod)(_.id)
    def * = (id, product, description) <> ((ProductDescription.apply _).tupled, ProductDescription.unapply)
  }

  def create(id: Long, productId: Long, description: String): Future[ProductDescription] = db.run {
    (productDescription.map(c => (c.product, c.description))
      returning productDescription.map(_.id)
      into {case ((product, description), id) => ProductDescription(id, product, description)}
      ) += (productId, description)
  }

  def list(): Future[Seq[ProductDescription]] = db.run {
    productDescription.result
  }

  def getById(id: Long): Future[ProductDescription] = db.run{
    productDescription.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    productDescription.filter(_.id === id).delete
  }

  def update(id: Long, newProductDescription: ProductDescription): Future[Unit] = {
    val productToUpdate: ProductDescription = newProductDescription.copy(id, newProductDescription.productId, newProductDescription.description)
    db.run {
      productDescription.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}
