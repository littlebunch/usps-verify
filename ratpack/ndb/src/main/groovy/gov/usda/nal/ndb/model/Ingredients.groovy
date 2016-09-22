package gov.usda.nal.ndb.model
/**
 * <code>Ingredients</code> is for food ingredients listed in the Branded Food Products Database
 * @author Gary.Moore
 *
 */

 import app.RatpackGormEntity
class Ingredients implements  RatpackGormEntity<Ingredients> {
	Date available	//	The date the data for the food item represented by the specific GTIN was made available on the market.
	Date discontinued //	The data indicated by the manufacturer that the product represented by a specific GTIN has been discontinued
	Date updated  //The date the manufacturer last updated the data represented by the specific GTIN.
	String description // full text of the ingredient statement provided by the manufacturer
	Date lastUpdated
	static belongsTo=[food:Foods]
    static constraints = {
		available nullable:false,blank:false
		updated nullable:false,blank:false
		discontinued nullable:true,blank:true
		description nullable:false,blank:false
    }
	static mapping =
	{
		description sqlType:"VARCHAR(4096)"
	}
}
