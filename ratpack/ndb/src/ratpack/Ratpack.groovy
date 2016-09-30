import ratpack.groovy.template.MarkupTemplateModule
import javax.sql.DataSource
import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack
import static groovy.json.JsonOutput.toJson
import ratpack.exec.Blocking
import ratpack.service.Service
import ratpack.service.StartEvent
import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.services.UnitsService
import gov.usda.nal.ndb.services.DefaultUnitsService
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.services.FoodGroupsService
import gov.usda.nal.ndb.services.DefaultFoodGroupsService
import gov.usda.nal.ndb.services.DbBootStrapService
import groovy.json.JsonSlurper
import app.ApplicationConfig
import app.modules.GormModule

ratpack {
  serverConfig {
    json "dbconfig.json"
    yaml "dbconfig.yml"
    env()
    sysProps()
    require("",ApplicationConfig)
  }
  bindings {
    // load our application config from the serverConfig for use in bindings
    ApplicationConfig appConfig
    serverConfig.requiredConfig.each {
      if ( it.object instanceof ApplicationConfig) {
        appConfig=it.object
        return
      }
    }
    module MarkupTemplateModule
    // add GORM to the Registry
    moduleConfig(GormModule,appConfig)
    bindInstance UnitsService, new DefaultUnitsService()
    bindInstance FoodGroupsService, new DefaultFoodGroupsService()

    bindInstance JsonSlurper, new JsonSlurper()
    bind DbBootStrapService
    bindInstance new Service() {
      void onStart(StartEvent e) throws Exception {
        e.getRegistry().get(HibernateDatastoreSpringInitializer)
        Blocking.exec {
          new DbBootStrapService().initDb()
        }
      }
    }
  }

  handlers {
    path('api') {JsonSlurper jsonSlurper, FoodGroupsService foodGroupsService,UnitsService unitsService ->
      byMethod {
        post {
          request.body.map {body ->
            jsonSlurper.parseText(body.text) as Map
        }.map { data->
          new Units(data)
        }.flatMap { unit ->
          unitsService.save(unit)
        }.then { m ->
          response.contentType("application/json")
          response.send(m)
        }
      }
      get("units") {
          unitsService.getUnitsAsJson().then { u ->
              response.contentType("application/json")
              response.send(u)
          }
          response.status(400)
        }
        get("foodgroups") {
            foodGroupsService.getFoodGroupsAsJson().then { f ->
                response.contentType("application/json")
                response.send(f)
            }
            response.status(400)
          }
      }
    }
    get("units") { UnitsService unitsService ->
      unitsService.getUnitsAsJson().then { u ->
          response.contentType("application/json")
          response.send(u)
      }
      response.status(400)
    }
    get("foodgroups") { FoodGroupsService foodGroupsService ->
        foodGroupsService.getFoodGroupsAsJson().then { f ->
            response.contentType("application/json")
            response.send(f)
        }
        response.status(400)
      }
    get("config")
    {
      ApplicationConfig config ->
        render toJson(config)
    }
    get {
      render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    }

    files { dir "public" }
  }
}
