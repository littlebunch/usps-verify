package app
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormStaticApi
import ratpack.exec.Blocking
import ratpack.exec.Promise
/**
* Overloads withNewSession to automatically schedule Blocking
*/
@CompileStatic
trait RatpackGormEntity<D> extends GormEntity<D> {
  private static GormStaticApi<D> internalStaticApi

  static GormStaticApi<D> currentGormStaticApi()
  {
    if (internalStaticApi == null )
      internalStaticApi=(GormStaticApi<D>) GormEnhancer.findStaticApi(this)
    internalStaticApi
  }

  static<V> Promise<V> withNewSession(Closure callable) {
    Blocking.get {
      (V)currentGormStaticApi().withNewSession(callable)
    }
  }
}
