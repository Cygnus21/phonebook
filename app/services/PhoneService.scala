package services

import com.google.inject.Inject
import models.{Phone, PhoneList}
import scala.concurrent.Future

class PhoneService @Inject() (items: PhoneList) {

  def addItem(item: Phone): Future[String] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }

  def getItem(id: Long): Future[Option[Phone]] = {
    items.get(id)
  }

  def listItems(): Future[Seq[Phone]] = {
    items.listAll()
  }

  def updateItem(phoneItem: Phone): Future[Int] = {
    items.update(phoneItem)
  }

  def searchByName(name: String): Future[Seq[Phone]] = {
    items.searchByName(name)
  }

  def searchByPhone(phonenumber: String): Future[Seq[Phone]] = {
    items.searchByNumber(phonenumber)
  }

  def getByPhone(phonenumber: String): Future[Int] = {
    items.getByPhone(phonenumber);
  }

}
