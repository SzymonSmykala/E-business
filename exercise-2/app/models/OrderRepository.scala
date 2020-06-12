package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class OrderRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, basketRepository: BasketRepository, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import basketRepository.BasketTable;
  private val basketTable = TableQuery[BasketTable];

  import paymentRepository.PaymentTable
  private val paymentTable = TableQuery[PaymentTable]

  private val order = TableQuery[OrderTable]
  private class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def payment = column[Long]("payment_id")
    def basket = column[Long]("basket_id")
    def basketFk = foreignKey("basket_fk", basket, basketTable)(_.id);
    def paymentFk = foreignKey("payment_fk", payment, paymentTable)(_.id)
    def * = (id, basket, payment) <> ((Order.apply _).tupled, Order.unapply)
  }

  def create(id: Long, basketId: Long, paymentId: Long): Future[Order] = db.run {
    (order.map(c => (c.basket, c.payment))
      returning order.map(_.id)
      into {case ((basket_id, payment_id), id) => Order(id, basket_id, payment_id)}
      ) += (basketId, paymentId)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }

  def getById(id: Long): Future[Order] = db.run{
    order.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    order.filter(_.id === id).delete
  }

  def getByBasketId(id: Long): Future[Seq[Order]] = db.run {
    order.filter(_.basket === id).result
  }

  def update(id: Long, newOrder: Order): Future[Unit] = {
    val orderToUpdate: Order = newOrder.copy(id, newOrder.basketId, newOrder.paymentId)
    db.run {
      order.filter(_.id === id).update(orderToUpdate).map(_ => ())
    }
  }


}
