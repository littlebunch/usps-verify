package gov.usda.nal.ndb.services
import groovy.json.JsonBuilder
import ratpack.exec.Promise
import ratpack.exec.Operation
import ratpack.exec.Blocking
import org.apache.commons.httpclient.HttpStatus
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.model.Foods
import gov.usda.nal.ndb.model.Foods
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.model.NutrientData
class DefaultModelService implements ModelService {
//@Override
Promise<String> save(Object o)
  {
    def r="saved",
    s=HttpStatus.SC_OK
    switch(o)
    {
      case FoodGroups:
      case Foods:
      case Units:
        o.withNewSession {
          o.validate()
          if ( o.hasErrors()) {
            o.errors.allErrors().each{
              r+=it
            }
          } else
          {
            o.save(flush:true)
          }
        }
      break
      default:
        r="Unrecognized object type"
      break

  }
    Promise.sync{'{"status":'+s+',"msg":"'+r+'"}'}
  }
//  @Override
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
  //@Override
  Promise<Foods> getFood(String ndbno) {
    def f
      Food.withNewSession {
        f=Foods.findWhere(ndbNo:ndbno)
      }
      Promise.sync{f}
  }
  //@Override
  Promise<String> getFoodAsJson(List ndbno)
  {
    def j=new JsonBuilder()
      Foods.withNewSession {
        j {
          foods: ndbno.collect { nid ->
          def f=Foods.findWhere(ndbNo:nid)
          if ( f ) {
              def n=NutrientData.findAllWhere(food:f)
              food([ndbNo:nid,description:f.description,source:f.source,
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
                    footnotes: f.footnotes.collect{fn->
                      ['id':fn.fnId!=null?fn.fnId:'','desc':fn.fnText!=null?fn.fnText:'']
					          }.unique().sort(),
                    measures: f.weights.collect{ m ->
                        					[id:m.id,label:m.description,eqv:m.gramWeight,qty:m.amount,seq:m.seq]
  				          },
                    nutrients: n.collect{ d->
                      /*Integer dataPoints
                      Double standardError
                      String addNutMark
                      Integer numberStudies
                      Double minimum
                      Double maximum
                      Double degreesFreedom
                      Double lowerEB
                      Double upperEB
                      String comment
                      String confidenceCode
                      Date lastModified
                      SourceCode source
                      Derivation derivation*/
                          [nutrientNo:d.nutrient.nutrientNo,nutrient:d.nutrient.description,value:d.value,dp:d.dataPoints,
                          se:d.standardError,studies:d.numberStudies,min:d.minimum,max:d.maximum,lb:d.lowerEB,ub:d.upperEB,
                          comment:d.comment,confidenceCode:d.confidenceCode,source:[code:d.source.code,desc:d.source.description]]
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
//  @Override
    Promise<List<FoodGroups>> getFoodGroups() {
      def f
        FoodGroups.withNewSession {

          FoodGroups.list().collect(f) {
            [id:it.id,cd:it.cd,description:it.description]
          }
      }
        Promise.sync{f}
    }
    Promise<String> getFoodGroupsAsJson()
    {
      JsonBuilder j=new JsonBuilder()
      j {
        foodgroup(
            FoodGroups.withNewSession {
              FoodGroups.list().collect{
                [id:it.id,cd:it.cd,description:it.description,lastUpdated:it.lastUpdated]
            }}
        )
      }
      Promise.sync{ j.toString()}
    }
}
