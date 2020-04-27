package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class PaymentRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val payment = TableQuery[PaymentTable]
  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def status = column[String]("status")
    def * = (id, status) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  def create(id: Long, status: String): Future[Payment] = db.run {
    (payment.map(c => c.status)
      returning payment.map(_.id)
      into {case (status, id) => Payment(id, status)}
      ) += status
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def getById(id: Long): Future[Payment] = db.run{
    payment.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    payment.filter(_.id === id).delete
  }

  def update(id: Long, newPayment: Payment): Future[Unit] = {
    val productToUpdate: Payment = newPayment.copy(id, newPayment.status)
    db.run {
      payment.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}
