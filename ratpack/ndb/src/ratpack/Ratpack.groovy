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
    bindInstance UnitsService, new DefaultUnitsService()

    bindInstance JsonSlurper, new JsonSlurper()
    // add GORM to the Registry
    moduleConfig(GormModule,appConfig)

    bindInstance new Service() {
      void onStart(StartEvent e) throws Exception {
          e.getRegistry().get(HibernateDatastoreSpringInitializer)
          Blocking.exec {
            Units.withNewSession {
                new Units(unit:"g").save()
                new Units(unit:"lb").save()
                new Units(unit:"ml").save()
            }
          }
        }
      }
  }

  handlers {
    path('api') {JsonSlurper jsonSlurper, UnitsService userService ->
      byMethod {
        post {
          request.body.map {body ->
            jsonSlurper.parseText(body.text) as Map
        }.map { data->
          new Units(data)
        }.flatMap { user ->
          UnitsService.save(unit)
        }.then {
          response.send()
        }
      }

        get {
         /*UnitsService.getUnits().then { units->
              response.send(toJson(units))
          }*/
          Blocking.get {
            Units.withNewSession {
              Units.list().collect{ u->
                [id:u.id,version:u.version,unit:u.unit]
              }
            }
          } then { unitsList ->
              rendor toJson(unitsList.toString())
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
