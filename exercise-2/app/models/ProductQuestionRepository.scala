package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProductQuestionRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import productRepository.ProductTable
  private val prod = TableQuery[ProductTable]

  private val productQuestion = TableQuery[ProductQuestionTable]
  private class ProductQuestionTable(tag: Tag) extends Table[ProductQuestion](tag, "product_question") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product = column[Long]("product_id")
    def questionContent = column[String]("question_content")
    def product_fk = foreignKey("prod_fk",product, prod)(_.id)
    def * = (id, product, questionContent) <> ((ProductQuestion.apply _).tupled, ProductQuestion.unapply)
  }

  def create(id: Long, product_id: Long, questionContent: String): Future[ProductQuestion] = db.run {
    (productQuestion.map(c => (c.product, c.questionContent))
      returning productQuestion.map(_.id)
      into {case ((product, qustionContent), id) => ProductQuestion(id, product, qustionContent)}
      ) += (product_id, questionContent)
  }

  def list(): Future[Seq[ProductQuestion]] = db.run {
    productQuestion.result
  }

  def getById(id: Long): Future[ProductQuestion] = db.run{
    productQuestion.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    productQuestion.filter(_.id === id).delete
  }

  def update(id: Long, newProductDescription: ProductQuestion): Future[Unit] = {
    val productToUpdate: ProductQuestion = newProductDescription.copy(id, newProductDescription.productId, newProductDescription.questionContent)
    db.run {
      productQuestion.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}
