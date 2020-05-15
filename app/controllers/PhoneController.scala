package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Phone, PhoneForm}
import services.PhoneService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class PhoneController @Inject() (cc: ControllerComponents, phoneService: PhoneService)
extends AbstractController(cc) {
  implicit val phoneFormat = Json.format[Phone]

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listAll() = Action.async { implicit request: Request[AnyContent] =>
    phoneService.listItems map { items =>
      Ok(Json.toJson(items))
    }
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    PhoneForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val time: Duration = 5.seconds;
        val number = data.phonenumber;
        val result = Await.result(phoneService.getByPhone(number), time);
        result match {
          case 0 => {
            val newPhoneItem = Phone(0, data.name, data.phonenumber)
            phoneService.addItem(newPhoneItem).map { _ =>
              Redirect(routes.PhoneController.index())
            }
          }
          case 1 => Future.successful(BadRequest(s"Contact with number $number already exists in database"));
        }
        })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    val time: Duration = 5.seconds
    val result = Await.result(phoneService.getItem(id), time)
    result.map { res =>
      phoneService.deleteItem(id).map { _ =>
        Redirect(routes.PhoneController.index())
      }
    }.getOrElse(Future.successful(NotFound("Phone was not found")))
  }

  def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    PhoneForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val phoneItem = Phone(id, data.name, data.phonenumber)
        phoneService.updateItem(phoneItem).map{ _ =>
          Redirect(routes.PhoneController.index())
      }})
  }

  def searchByName() = Action.async { implicit request: Request[AnyContent] =>
    phoneService.searchByName(request.queryString.get("nameSubstring").toList.head.head) map { items =>
      Ok(Json.toJson(items))
    }
  }

  def searchByPhone() = Action.async { implicit request: Request[AnyContent] =>
    phoneService.searchByPhone(request.queryString.get("phoneSubstring").toList.head.head) map { items =>
      Ok(Json.toJson(items))
    }
  }
}
