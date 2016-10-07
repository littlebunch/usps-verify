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
import java.util.Date;
import java.sql.Timestamp;
import org.grails.datastore.gorm.GormEntity
import grails.gorm.annotation.Entity
import app.RatpackGormEntity
/**
 * @version $Id: Derivation.groovy 1503 2011-05-27 20:09:36Z  $
 * @author gmoore
 */

@Entity
class Derivation implements GormEntity<Derivation> {
	String code
	String description
	Date lastUpdated

    static constraints = {
		code blank:false,nullable:false
		description blank:false,nullable:false

    }
	static mapping = {
		code sqlType:"VARCHAR(8)",index:"derv_idx"
		description sqlType:"VARCHAR(150)"

	}
}
