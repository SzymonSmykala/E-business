package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PromoRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext, productRepository: ProductRepository) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import productRepository.ProductTable

  private val productTable = TableQuery[ProductTable]
  private val promo = TableQuery[PromoTable]

  class PromoTable(tag: Tag) extends Table[Promo](tag, "promo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product_id")
    def looseAmount = column[Int]("loose_amount")
    private def product_fk = foreignKey("prod_fk", product, productTable)(_.id)
    def * = (id, product, looseAmount) <> ((Promo.apply _).tupled, Promo.unapply)
  }

  def create(id: Long, productId: Long, looseAmount: Int): Future[Promo] = db.run {
    (promo.map(c => (c.product, c.looseAmount))
      returning promo.map(_.id)
      into {case ((productId, looseAmount), id) => Promo(id, productId, looseAmount)}
      ) += (productId, looseAmount)
  }

  def list(): Future[Seq[Promo]] = db.run {
    promo.result
  }

  def getById(id: Long): Future[Promo] = db.run{
    promo.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    promo.filter(_.id === id).delete
  }

  def update(id: Long, newPromo: Promo): Future[Unit] = {
    val productToUpdate: Promo = newPromo.copy(id, newPromo.productId, newPromo.looseAmount)
    db.run {
      promo.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}

