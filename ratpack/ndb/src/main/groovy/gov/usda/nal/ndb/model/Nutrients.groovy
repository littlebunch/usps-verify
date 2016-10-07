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

import java.util.Date
import java.sql.Timestamp
import org.grails.datastore.gorm.GormEntity
import grails.gorm.annotation.Entity
import app.RatpackGormEntity

/**
* Domain for nutrient definition objects.  Source file is NUTR_DEF.txt
* @author gmoore
* @version $Id: Nutrients.groovy 1541 2011-06-07 16:50:49Z  $
*/

@Entity
class Nutrients implements GormEntity<Nutrients>{
	Long id,version
	String nutrientNo
	String tagName
	String description
	Integer decimalPoint
	Integer srNutOrder
	Units	unit
	NutrientType type
	Date lastUpdated


	static belongsTo=[Units,NutrientType]
	static hasMany=[footnotes:FootNote]
    static constraints = {

		nutrientNo blank:false,nullable:false
		description blank:false,nullable:false
		type nullable:true,blank:true
    }
	static mapping ={
		nutrientNo sqlType:"VARCHAR(4)", index:"nutr_idx"
		tagName sqlType:"VARCHAR(56)"
		description sqlType:"VARCHAR(120)"

	}
}
