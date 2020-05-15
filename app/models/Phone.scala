package models
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

case class Phone(id: Long, name: String, phonenumber: String)

case class PhoneFormData(name: String, phonenumber: String)

object PhoneForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "phonenumber" -> nonEmptyText
    ) (PhoneFormData.apply)(PhoneFormData.unapply)
  )
}

class PhoneTableDef(tag: Tag) extends Table[Phone](tag, "phonebook") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def phonenumber = column[String]("phonenumber")

  override def * = (id, name, phonenumber) <> (Phone.tupled, Phone.unapply)
}

class PhoneList @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) (
                          implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {
  var phoneList = TableQuery[PhoneTableDef]

  def add(phoneItem: Phone): Future[String] = {
      dbConfig.db
        .run(phoneList += phoneItem)
        .map(res => "Phone was successfully added")
        .recover {
          case ex: Exception => {
            printf(ex.getMessage())
            ex.getMessage
          }
        }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(phoneList.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Phone]] = {
    dbConfig.db.run(phoneList.filter(_.id === id).result.headOption)
  }

  def listAll(): Future[Seq[Phone]] = {
    dbConfig.db.run(phoneList.result)
  }

  def update(phoneItem: Phone): Future[Int] = {
    dbConfig.db.run(
      phoneList.filter(_.id === phoneItem.id)
        .map(x => (x.name, x.phonenumber))
        .update(phoneItem.name, phoneItem.phonenumber)
    )
  }

  def searchByName(name: String): Future[Seq[Phone]] = {
    dbConfig.db.run(phoneList.filter(_.name === name).result)
  }

  def searchByNumber(phonenumber: String): Future[Seq[Phone]] = {
    dbConfig.db.run(phoneList.filter(_.phonenumber === phonenumber).result)
  }

  def getByPhone(phonenumber: String): Future[Int] = {
    dbConfig.db.run(phoneList.filter(_.phonenumber === phonenumber).length.result)
  }
}