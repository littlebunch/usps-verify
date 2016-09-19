package gov.usda.nal.ndb
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.Units
interface UnitsService {
    Promise<Void> save(Units unit)
    Promise<List<Units>> getUnits()
}
