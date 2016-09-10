package gov.usda.nal.ndb.model
import org.grails.datastore.gorm.GormEntity
class User implements GormEntity<User> {
  Long id
  Long version
  String name
  String email
  static constraints ={
    name blank:false,nullable:false
  }
}
