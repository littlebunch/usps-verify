package gov.usda.nal.ndb.services
import ratpack.server.Service
import ratpack.server.StartEvent
import ratpack.server.StopEvent
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.model.FoodGroups
/**
* Use this to perform any database bootstraping.
*
*/
class DbBootStrapService implements Service {
  void initDb() {
    initUnits()
    initFoodGroups()
  }
  void initUnits() {
        Units.withNewSession {
          Units.findOrSaveWhere(unit:'g')
          Units.findOrSaveWhere(unit:'lb')
          Units.findOrSaveWhere(unit:'gal')
          Units.findOrSaveWhere(unit:'ml' )
          Units.findOrSaveWhere(unit:'ton')
      }
  }
  void initFoodGroups() {
    FoodGroups.withNewSession {
      FoodGroups.findOrSaveWhere(cd:'4500',description:'Branded Food Products')
    }
  }

}
