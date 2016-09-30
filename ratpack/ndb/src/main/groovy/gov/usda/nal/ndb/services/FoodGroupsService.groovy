package gov.usda.nal.ndb.services
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.FoodGroups
interface FoodGroupsService {
    Promise<String> save(FoodGroups f)
    Promise<List<FoodGroups>> getFoodGroups()
    Promise<String>getFoodGroupsAsJson()
}
