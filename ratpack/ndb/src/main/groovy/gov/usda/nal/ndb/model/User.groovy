package gov.usda.nal.ndb.model
import org.grails.datastore.gorm.GormEntity
import app.RatpackGormEntity
class User implements GormEntity<User> {
  Long id
  Long version
  String name
  String email
  static constraints ={
    name nullable:false
    email nullable:false
  }
}
