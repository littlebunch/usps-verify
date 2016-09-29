package gov.usda.nal.ndb
import groovy.json.JsonBuilder
import ratpack.exec.Promise
import ratpack.exec.Operation
import gov.usda.nal.ndb.model.Units
class DefaultUnitsService implements UnitsService {
@Override
Promise<Void> save(Units unit)
  {
    Unit.withSession {
      Units.save(unit)
    }
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
