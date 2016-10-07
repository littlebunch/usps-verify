package gov.usda.nal.ndb.services
import groovy.json.JsonBuilder
import ratpack.exec.Promise
import ratpack.exec.Operation
import ratpack.exec.Blocking
//import org.apache.commons.httpclient.HttpStatus
import gov.usda.nal.ndb.model.FoodGroups
class DefaultFoodGroupsService implements FoodGroupsService {
@Override
Promise<String> save(FoodGroups f)
  {
    def r="saved",
    s=200 //HttpStatus.SC_OK
    FoodGroups.withNewSession {
      f.validate()
      if ( f.hasErrors()) {
        f.errors.allErrors().each{
          r+=it
        }
      }
      f.save()
    }
    Promise.sync{'{"status":'+s+',"msg":"'+r+'"}'}
  }
  @Override
  Promise<List<FoodGroups>> getFoodGroups() {
    def f
      FoodGroups.withNewSession {

        FoodGroups.list().collect(f) {
          [id:it.id,cd:it.cd,description:it.description]
        }
    }
      Promise.sync{f}
  }
  Promise<String> getFoodGroupsAsJson()
  {
    JsonBuilder j=new JsonBuilder()
    j {
      foodgroup(
          FoodGroups.withNewSession {
            FoodGroups.list().collect{
              [id:it.id,cd:it.cd,description:it.description,lastUpdated:it.lastUpdated]
          }}
      )
    }
    Promise.sync{ j.toString()}
  }
}
