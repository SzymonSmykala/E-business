package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class BasketItemRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, basketRepository: BasketRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import basketRepository.BasketTable;
  private val basketTable = TableQuery[BasketTable];

  import productRepository.ProductTable
  private val productTable = TableQuery[ProductTable]

  private val basketItem = TableQuery[BasketItemTable]
  private class BasketItemTable(tag: Tag) extends Table[BasketItem](tag, "basket_item") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product_id")
    def count = column[Int]("count")
    def basket = column[Long]("basket_id")
    def basketFk = foreignKey("basket_fk", basket, basketTable)(_.id);
    def productFk = foreignKey("product_fk", product, productTable)(_.id)
    def * = (id, product, count, basket) <> ((BasketItem.apply _).tupled, BasketItem.unapply)
  }

  def create(id: Long, producId: Long, count: Int, basketId: Long): Future[BasketItem] = db.run {
    (basketItem.map(c => (c.product, c.count, c.basket))
      returning basketItem.map(_.id)
      into {case ((product_id, count, basket_id), id) => BasketItem(id,product_id, count, basket_id)}
      ) += (producId, count, basketId)
  }

  def list(): Future[Seq[BasketItem]] = db.run {
    basketItem.result
  }

  def getById(id: Long): Future[BasketItem] = db.run{
    basketItem.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    basketItem.filter(_.id === id).delete
  }

  def update(id: Long, newBasket: BasketItem): Future[Unit] = {
    val basketToUpdate: BasketItem = newBasket.copy(id, newBasket.productId, newBasket.count, newBasket.basketId)
    db.run {
      basketItem.filter(_.id === id).update(basketToUpdate).map(_ => ())
    }
  }

  def getBasketItemsByBasketId(basketId: Long): Future[Seq[BasketItem]] = db.run {
    basketItem.filter(_.basket === basketId).result
  }

}
