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
import gov.usda.nal.ndb.model.FoodGroups
import gov.usda.nal.ndb.model.Foods
import gov.usda.nal.ndb.services.DbBootStrapService
import gov.usda.nal.ndb.services.ModelService
import gov.usda.nal.ndb.services.DefaultModelService
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

    bindInstance ModelService, new DefaultModelService()
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
    path('api') {JsonSlurper jsonSlurper, ModelService modelService ->
      byMethod {
        post {
          request.body.map {body ->
            jsonSlurper.parseText(body.text) as Map
        }.map { data->
          new Units(data)
        }.flatMap { unit ->
          modelService.save(unit)
        }.then { m ->
          response.contentType("application/json")
          response.send(m)
        }
      }
      get("units") {
          modelService.getUnitsAsJson().then { u ->
              response.contentType("application/json")
              response.send(u)
          }
          response.status(400)
        }
        get("foodgroups") {
            modelService.getFoodGroupsAsJson().then { f ->
                response.contentType("application/json")
                response.send(f)
            }
            response.status(400)
          }

      }
    }
    get("units") { ModelService modelService ->
      Blocking.get {
        modelService.getUnitsAsJson()
      } then { u ->
          render(u)
      }
      response.status(400)
    }
    get("foodgroups") { ModelService modelService ->
      Blocking.get {
        modelService.getFoodGroupsAsJson() } then { f ->
            render(f)
        }
        response.status(400)
      }
    get("food") {JsonSlurper jsonSlurper, ModelService modelService ->
      byContent {
        json {
          request.body.map {body ->
              jsonSlurper.parseText(body.text) as Map
          }.map { data->
              modelService.getFoodAsJson(data.ndbno)
          }.then { f ->
              render(f)
          }
        }
        html {
          render(modelService.getFoodAsJson(request.queryParams.ndbno?:"9999999"))
        }
      }
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
