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
import app.RatpackGormEntity

/**
 * @version $Id: NutrientType.groovy 1472 2011-05-25 20:33:48Z  $
 * @author gmoore
 */

class NutrientType implements  RatpackGormEntity<NutrientType>{
	String type
	Date lastUpdated
    static constraints = {
		type blank:false,nullable:false


    }
	static mapping =
	{
		type sqlType:"VARCHAR(50)"

	}
}
