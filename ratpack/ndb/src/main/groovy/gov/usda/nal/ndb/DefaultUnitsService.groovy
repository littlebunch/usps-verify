package gov.usda.nal.ndb
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
      Units.withNewSession {
        Units.list().collect {
          [id:it.id,version:it.version,unit:it.unit]
        }
      }
  }
}
