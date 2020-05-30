package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  val category = TableQuery[CategoryTable]
  class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  def create(id: Long, name: String): Future[Category] = db.run {
    (category.map(c => c.name)
      returning category.map(_.id)
      into {case (name, id) => Category(id, name)}
      ) += name
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def getById(id: Long): Future[Category] = db.run{
    category.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Int] = db.run {
    category.filter(_.id === id).delete
  }

  def update(id: Long, newCategory: Category): Future[Unit] = {
    val productToUpdate: Category = newCategory.copy(id, newCategory.name)
    db.run {
      category.filter(_.id === id).update(productToUpdate).map(_ => ())
    }
  }

}
