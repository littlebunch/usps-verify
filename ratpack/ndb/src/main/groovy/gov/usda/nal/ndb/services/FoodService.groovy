package gov.usda.nal.ndb.services
import ratpack.exec.Promise
import gov.usda.nal.ndb.model.Foods
interface FoodService {
    Promise<String> save(Foods f)
    Promise<Foods> getFood(String ndbno)
    Promise<String>getFoodAsJson(String ndbno)
    Promise<List<Foods>>list()
}
