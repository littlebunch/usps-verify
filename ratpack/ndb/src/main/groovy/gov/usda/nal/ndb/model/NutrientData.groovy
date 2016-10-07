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

/**
* @author gmoore
* @version $Id: NutrientData.groovy 1926 2011-10-06 18:40:06Z  $
*/

import java.util.Date;
import java.sql.Timestamp;
import org.grails.datastore.gorm.GormEntity
import grails.gorm.annotation.Entity
import app.RatpackGormEntity
@Entity
class NutrientData implements GormEntity<NutrientData> {
	Double value
	Integer dataPoints
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
	Derivation derivation
	Foods refNDB
	Foods food
	Nutrients nutrient
	Date lastUpdated

	//static belongsTo=[source:SourceCode,derivation:Derivation,refNDB:Foods,food:Foods,nutrient:Nutrients]
	static constraints = {
		value nullable:false
		dataPoints nullable:false
		refNDB nullable:true,blank:true
		maximum nullable:true ,blank:true
		minimum nullable:true ,blank:true


    }
	static mapping = {
		addNutMark sqlType:"VARCHAR(2)"
		comment sqlType:"VARCHAR(20)"
		confidenceCode sqlType:"VARCHAR(1)"

	}
}
