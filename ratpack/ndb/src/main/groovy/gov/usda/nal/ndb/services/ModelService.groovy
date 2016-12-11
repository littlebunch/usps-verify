package gov.usda.nal.ndb.services
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.model.Foods
interface ModelService {
    Promise<String> save(Object o)
    Promise<List<Units>> getUnits()
    Promise<List<FoodGroups>>getFoodGroups()
    Promise<List<Foods>>getFood(String ndb)
    Promise<String>getUnitsAsJson()
    Promise<String>getFoodGroupsAsJson()
    Promise<String>getFoodAsJson(List ndbno)
}
