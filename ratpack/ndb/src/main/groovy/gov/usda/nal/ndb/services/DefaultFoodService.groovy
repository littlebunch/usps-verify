package gov.usda.nal.ndb.services
import groovy.json.JsonBuilder
import ratpack.exec.Promise
import ratpack.exec.Operation
import ratpack.exec.Blocking
import org.springframework.transaction.annotation.*
//import org.apache.commons.httpclient.HttpStatus
import gov.usda.nal.ndb.model.Foods
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.model.NutrientData
class DefaultFoodService implements FoodService {
@Override
Promise<String> save(Foods f)
  {
    def r="saved",
    s=200 //HttpStatus.SC_OK
    Foods.withNewSession {
      f.validate()
      if ( f.hasErrors()) {
        f.errors.allErrors().each{
          r+=it
        }
      }
      f.save()
    }
    Promise.sync{'{"status":'+s+',"msg":"'+r+'"}'}
  }
  @Override
  Promise<Foods> getFood(String ndbno) {
    def f
      Food.withNewSession {
        f=Foods.findWhere(ndbNo:ndbno)
      }
      Promise.sync{f}
  }
  @Transactional
  Promise<String> getFoodAsJson(String ndbno)
  {
    def j=new JsonBuilder()
    Blocking.exec {
      Foods.withNewSession {
        def f=Foods.findWhere(ndbNo:ndbno)
        def n=NutrientData.findAllWhere(food:f)
        j {
          if ( f ) {
              food([ndbNo:ndbno,description:f.description,source:f.source,
                    shortdescript:f.shortDescription,
                    scientificName:f.scientificName,
                    commercialName:f.commercialName,
                    refuse:f.refuse,
                    refuseDescription:f.refuseDescription,
                    nFactor:f.nFactor,
                    proFactor:f.proFactor,
                    fatFactor:f.fatFactor,
                    choFactor:f.choFactor,
                    group:f.fdGroup.description,
                    manu:f.manufacturer.name,
                    ingredients:['desc':f.ingredients?.description],
                    measures: f.weights.collect{ m ->
                        					[id:m.id,label:m.description,eqv:m.gramWeight,qty:m.amount,seq:m.seq]
  				                     },
                    nutrients: n.collect{ d->
                                  [nutrientNo:d.nutrient.nutrientNo,nutrient:d.nutrient.description,value:d.value]
                                }
                  ])
          } else {
            food([ndbNo:ndbno,description:"Not Found"])
          }
          }
      }
    }
    Promise.sync{ j.toString()}
  }
  Promise<List<Foods>>list()
  {

  }
}
