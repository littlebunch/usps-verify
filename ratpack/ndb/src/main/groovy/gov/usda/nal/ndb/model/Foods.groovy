/** ===========================================================================
*
*                            PUBLIC DOMAIN NOTICE
*               		National Agriculture Library
*
*  This software/database is a "United States Government Work" under the
*  terms of the United States Copyright Act.  It was written as part of
*  the author's official duties as a United States Government employee and
*  thus cannot be copyrighted.  This software/database is freely available
*  to the public for use. The National Agriculture Library and the U.S.
*  Government have not placed any restriction on its use or reproduction.
*
*  Although all reasonable efforts have been taken to ensure the accuracy
*  and reliability of the software and data, the NAL and the U.S.
*  Government do not and cannot warrant the performance or results that
*  may be obtained by using this software or data. The NAL and the U.S.
*  Government disclaim all warranties, express or implied, including
*  warranties of performance, merchantability or fitness for any particular
*  purpose.
*
*  Please cite the author in any work or product based on this material.
*
*===========================================================================
*/
package gov.usda.nal.ndb.model

import app.RatpackGormEntity
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import gov.usda.nal.ndb.Source

/**
* @author gmoore
* @version $Id: Foods.groovy 1836 2011-09-01 20:35:37Z  $
*/

class Foods implements  RatpackGormEntity<Foods> {
	Long id
  Long version
	String ndbNo
	String description
	String shortDescription
	String commercialName
	String refuseDescription
	Source source
	Integer refuse
	String scientificName
	Double nFactor
	Double proFactor
	Double fatFactor
	Double choFactor
	Date lastUpdated

	static hasOne = [ingredients:Ingredients]
	static belongsTo = [fdGroup:FoodGroups,manufacturer:Manufacturer]
	static hasMany=[langual:Langual,
					footnotes:FootNote,
					weights:Weights]
	Boolean survey


    static constraints = {
		ndbNo blank:false,nullable:false
		description blank:false,nullable:false

    }
	static mapping ={
		ndbNo sqlType:"VARCHAR(8)",column:'ndb_no',index:'ndbno_idx'
		description sqlType:"VARCHAR(255)",index:'food_idx,all_name_idx'
		shortDescription sqlType:"VARCHAR(120)",index:'all_name_idx'
		commercialName sqlType:"VARCHAR(120)",index:'all_name_idx'
		refuseDescription sqlType:"VARCHAR(255)"
		scientificName sqlType:"VARCHAR(100)"
		source(enumType: "string")

	}
	/**
	* Returns a list of maps describing the measures available for a
	* foods object.  The list is sorted by label ( description)
	* @param f
	* @return
	*/
   List fetchMeasuresList()
   {
	   List rt=new ArrayList()
	  this.weights.each {
		   rt.add([id:it.id,label:it.description,eqv:it.gramWeight,qty:it.amount,seq:it.seq])
	   }
	  return rt.sort{it.seq}

   }
   /**
    *
    */
   Map fetchMeasureBySeq(int seq)
   {
	  Map rt=new HashMap()
	   this.weights.each
	   {
		   if ( it.seq == seq )
		   {
			   rt=[id:it.id,label:it.description,eqv:it.gramWeight,qty:it.amount,seq:it.seq]
			   return
		   }
	   }
	   return rt
   }

}
