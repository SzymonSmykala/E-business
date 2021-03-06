package controllers

import javax.inject.{Inject, _}
import models.{Category, CategoryRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class ProductController @Inject()(productsRepository: ProductRepository,  categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "category" -> number,
            "price" -> number
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }
  val productFormUpdate: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "category" -> number,
            "price" -> number
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def addProduct()(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.list()
    categories.map (cat => Ok(views.html.productadd(productForm, cat)))
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val items = productsRepository.list()
    items.map( p => Ok(views.html.products(p)))
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action { implicit request =>
      productsRepository.delete(id)
      Redirect(routes.ProductController.getProducts())
  }

  def updateProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var categories:Seq[Category] = Seq[Category]()
    categoryRepo.list().onComplete{
      case Success(cat) => categories = cat
      case Failure(_) => print("fail")
    }
    val produkt = productsRepository.getById(id)
    produkt.map(product => {
      val prodForm = productFormUpdate.fill(UpdateProductForm(product.id.toInt, product.name,product.categoryId.toInt, product.price))
      Ok(views.html.productupdate(prodForm, categories))
    })
  }

  def updateProductHandle(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var categ:Seq[Category] = Seq[Category]()
    categoryRepo.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    productFormUpdate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productupdate(errorForm, categ))
        )
      },
      product => {
        productsRepository.update(product.id, Product(product.name, product.category, product.id, product.price)).map { _ =>
          Redirect(routes.ProductController.updateProduct(product.id)).flashing("success" -> "product updated")
        }
      }
    )

  }

  def addProductHandle() = Action.async { implicit request =>
    var categ:Seq[Category] = Seq[Category]()

    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productadd(errorForm, categ))
        )
      },
      product => {
        productsRepository.create(product.name, product.category.toLong, 1, product.price).map { _ =>
          Ok("Ok")
        }
      }
    )

  }


  def get(id: Long) = Action.async {
    val result = productsRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    val updateResult = productsRepository.update(product.id, product)
    updateResult map {r => Ok(Json.toJson(product))}
  }

  def add():Action[AnyContent] = Action.async{ implicit request: MessagesRequest[AnyContent]  =>
      val json = request.body.asJson.get
      val product = json.as[Product]
      val inserted : Future[Product] = productsRepository.create(product.name, product.categoryId, product.id, product.price)
      inserted map {
        i =>
          Ok(Json.toJson(i))
      }
  }

  def readAll: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productsRepository.list()
    products.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = productsRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }
}
case class CreateProductForm(var name: String, var category: Int, var price: Int)
case class UpdateProductForm(var id: Int, var name: String, var category: Int, var price: Int)
