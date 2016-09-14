package gov.usda.nal.ndb
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.Units
class DefaultUnitsService implements UnitsService {
  private final List<Units> storage=[]
  @Override
  Promise<Void> save(Units unit)
  {
    storage << unit
    Promise.sync{null}
  }
  Promise<List<Units>> getUnits() {
    Promise.sync{Units.list()}
  }
}
