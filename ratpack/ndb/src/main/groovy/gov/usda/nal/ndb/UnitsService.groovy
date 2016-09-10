package gov.usda.nal.ndb
import gov.usda.nal.ndb.model.Units
import ratpack.exec.Promise
interface UnitsService {
    Promise<Void> save(Units unit)
    Promise<List<Units>> getUnits()
}
