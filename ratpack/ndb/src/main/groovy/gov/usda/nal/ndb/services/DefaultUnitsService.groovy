package gov.usda.nal.ndb.services
import groovy.json.JsonBuilder
import ratpack.exec.Promise
import ratpack.exec.Operation
//import org.apache.commons.httpclient.HttpStatus
import gov.usda.nal.ndb.model.Units
class DefaultUnitsService implements UnitsService {
@Override
Promise<String> save(Units u)
  {
    def r="saved",
    s=200 //HttpStatus.SC_OK
    Units.withNewSession {
      u.validate()
      if ( u.hasErrors()) {
        u.errors.allErrors().each{
          r+=it
        }
      }
      u.save()
    }
    Promise.sync{'{"status":'+s+',"msg":"'+r+'"}'}
  }
  @Override
  Promise<List<Units>> getUnits() {
    def u
      Units.withNewSession {
        Units.list().collect(u) {
          [id:it.id,version:it.version,unit:it.unit]
        }
      }
      Promise.sync{u}
  }
  Promise<String> getUnitsAsJson()
  {
    JsonBuilder j=new JsonBuilder()
    Units.withNewSession {
      j {

      units( Units.list().collect
        {
          [id:it.id,unit:it.unit]
        })
      }
    }
    Promise.sync{ j.toString()}
  }
}
