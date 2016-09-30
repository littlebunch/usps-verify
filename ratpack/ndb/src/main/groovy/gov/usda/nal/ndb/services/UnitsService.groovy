package gov.usda.nal.ndb.services
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.Units
interface UnitsService {
    Promise<String> save(Units u)
    Promise<List<Units>> getUnits()
    Promise<String>getUnitsAsJson()
}
