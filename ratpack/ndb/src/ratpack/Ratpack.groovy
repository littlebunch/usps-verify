import ratpack.groovy.template.MarkupTemplateModule
import javax.sql.DataSource
import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack
import static groovy.json.JsonOutput.toJson
import ratpack.exec.Blocking
/*import ratpack.hikari.HikariModule
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;*/
import ratpack.service.Service
import ratpack.service.StartEvent
import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.UnitsService
import gov.usda.nal.ndb.DefaultUnitsService
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

    bindInstance JsonSlurper, new JsonSlurper()
    bindInstance new Service() {
      void onStart(StartEvent e) throws Exception {
          e.getRegistry().get(HibernateDatastoreSpringInitializer)
            Units.withNewSession {
              Units.findOrSaveWhere(unit:'g')
              Units.findOrSaveWhere(unit:'lb')
              Units.findOrSaveWhere(unit:'gal')
              Units.findOrSaveWhere(unit:'ml' )
          }
        }
      }
  }

  handlers {
    path('api') {JsonSlurper jsonSlurper, UnitsService unitsService ->
      byMethod {
        post {
          request.body.map {body ->
            jsonSlurper.parseText(body.text) as Map
        }.map { data->
          new Units(data)
        }.flatMap { unit ->
          UnitsService.save(unit)
        }.then {
          response.send()
        }
      }
      get {
        unitsService.getUnits().then { u ->
          response.contentType("application/json")
          response.send(toJson(u))
        }
        response.status(400)
      }
      }
    }
    get("units") {
      UnitsService.getUnits().collect { u ->
        [id:u.id,version:u.version,unit:u.unit]
        println u
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
