import ratpack.groovy.template.MarkupTemplateModule
import ratpack.error.ServerErrorHandler
import ratpack.handling.Context
import javax.sql.DataSource
import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack
import static groovy.json.JsonOutput.toJson
import ratpack.exec.Blocking
import ratpack.service.Service
import ratpack.service.StartEvent
import ratpack.ssl.SSLContexts
import java.nio.file.Paths
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
    ssl SSLContexts.sslContext(Paths.get("/etc/server.jks"),"changeit")
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
    bindInstance ServerErrorHandler, new ServerErrorHandler() {
        @Override
        void error(Context ctx,Throwable t) throws Exception {
          def e="Sever error"
          println "t=${t}"
          ctx.response.status(500)
          if (t instanceof groovy.json.JsonException)
              e="JSON Parse Error"
          ctx.response.contentType("application/json")
          ctx.render('{"error":"'+e+'"}')
        }
    }
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
    path('api/units') {JsonSlurper jsonSlurper, ModelService modelService ->
      byMethod {
        post {
          request.body.map {body ->
            jsonSlurper.parseText(body.text) as Map
          }.map { data->
            new Units(data)
          }.flatMap { unit ->
            Blocking.get {
              modelService.save(unit)
          }.then { m ->
            response.contentType("application/json")
            render(m)
          }}
        }
        get {
          Blocking.get {
            modelService.getUnitsAsJson() }then { u ->
              response.contentType("application/json")
              render(u)
            }
            response.status(400)
        }
      }
    }
    path('api/foodgroups') { ModelService modelService ->
      byMethod {
        get{
            Blocking.get {
              modelService.getFoodGroupsAsJson() } then { f ->
                response.contentType("application/json")
                render(f)
              }
          response.status(400)
        }
      }
    }
    path('api/food') { JsonSlurper jsonSlurper, ModelService modelService ->
      byMethod {
        get {
          byContent {
            json {
              request.body.map {body ->
                  jsonSlurper.parseText(body.text) as Map
              }.map { data->
                println "data=${data}"
                Blocking.get {
                  modelService.getFoodAsJson(data.ndbno)
              }} then { f ->
                  response.contentType("application/json")
                  render(f)
              }
            }
            html {
              Blocking.get{
                modelService.getFoodAsJson(request.queryParams.ndbno?:"9999999")
              } then { f->
                  render(f)
                }
            }
          }
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
