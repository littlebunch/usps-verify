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
 * @version $Id: Units.groovy 1456 2011-05-24 11:36:00Z  $
 * @author gmoore
 */

import java.util.Date;
import java.sql.Timestamp;
import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity
import app.RatpackGormEntity

@Entity
class Units implements GormEntity<Units> {
	Long id,version
	String unit
	Date lastUpdated
	static hasMany=[nutrients:Nutrients]
    static constraints = {
			unit blank:false,unique:true
    }
}
