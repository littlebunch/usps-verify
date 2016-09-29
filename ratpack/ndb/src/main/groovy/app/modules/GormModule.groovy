package app.modules
import javax.sql.DataSource
import com.google.inject.Provides
import com.google.inject.Singleton
import ratpack.guice.ConfigurableModule
import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.jdbc.datasource.DriverManagerDataSource
import gov.usda.nal.ndb.model.Units
import gov.usda.nal.ndb.model.User
import gov.usda.nal.ndb.model.Foods
import gov.usda.nal.ndb.model.FoodGroups
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
/**
* Creates a HikariDataSoure in the GORM context
*/
class GormModule extends ConfigurableModule<app.ApplicationConfig> {
  @Override
  protected void configure()
  {

  }
  @Provides
  @Singleton
  GenericApplicationContext genericApplicationContext()
  {
    new GenericApplicationContext()
  }
/*  <beans:bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource"  destroy-method="close">
               <beans:property name="dataSourceClassName" value="com.mysql.jdbc.jdbc2.optional.MysqlDataSource"/>
               <beans:property name="maximumPoolSize" value="5" />
               <beans:property name="maxLifetime" value="30000" />
               <beans:property name="idleTimeout" value="30000" />
               <beans:property name="dataSourceProperties">
                         <beans:props>
                             <beans:prop key="url">jdbc:mysql://localhost:3306/exampledb</beans:prop>
                             <beans:prop key="user">root</beans:prop>
                             <beans:prop key="password"></beans:prop>
                              <beans:prop key="prepStmtCacheSize">250</beans:prop>
                              <beans:prop key="prepStmtCacheSqlLimit">2048</beans:prop>
                              <beans:prop key="cachePrepStmts">true</beans:prop>
                              <beans:prop key="useServerPrepStmts">true</beans:prop>
                         </beans:props>
               </beans:property>
</beans:bean>*/
  @Provides
  @Singleton
  HikariDataSource dataSource(GenericApplicationContext appCtx, app.ApplicationConfig appConfig)
  {
    HikariDataSource datasource=new HikariDataSource(new HikariConfig(/*dataSourceClassName:appConfig.database.dataSourceClass,*/
                              jdbcUrl:appConfig.database.url,
                              username:appConfig.database.user,
                              password:appConfig.database.password,
                              driverClassName:appConfig.database.driver))
    appCtx.beanFactory.registerSingleton 'dataSource', datasource
    datasource
  }
  @Provides
  @Singleton
  HibernateDatastoreSpringInitializer initializer(HikariDataSource dataSource,GenericApplicationContext appCtx) {
    def datastoreInitializer = new HibernateDatastoreSpringInitializer([Units,Foods,FoodGroups,User])
    //  def datastoreInitializer = new HibernateDatastoreSpringInitializer(FoodGroups)
    //    def datastoreInitializer = new HibernateDatastoreSpringInitializer(User)
    datastoreInitializer.configureForBeanDefinitionRegistry(appCtx)
    appCtx.refresh()
    datastoreInitializer
  }
}
