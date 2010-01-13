/*
 * Copyright (C) 2007-2009 Siemens AG
 *
 * This program and its interfaces are free software;
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.siemens.ct.exi.values;

import com.siemens.ct.exi.Constants;

public class BooleanValue extends AbstractValue {

	protected final boolean bool;

	public BooleanValue(boolean bool) {
		this.bool = bool;
	}
	
	public BooleanValue(int boolID) {
		switch (boolID) {
		case 0:
			characters = Constants.XSD_BOOLEAN_FALSE_ARRAY;
			sValue = Constants.XSD_BOOLEAN_FALSE;
			bool = false;
			break;
		case 1:
			characters = Constants.XSD_BOOLEAN_0_ARRAY;
			sValue = Constants.XSD_BOOLEAN_0;
			bool = false;
			break;
		case 2:
			characters = Constants.XSD_BOOLEAN_TRUE_ARRAY;
			sValue = Constants.XSD_BOOLEAN_TRUE;
			bool = true;
			break;
		case 3:
			characters = Constants.XSD_BOOLEAN_1_ARRAY;
			sValue = Constants.XSD_BOOLEAN_1;
			bool = true;
			break;
		default:
			throw new RuntimeException("Error while decoding boolean pattern facet");
		}
	}
	
	public boolean toBoolean() {
		return bool;
	}

	public char[] toCharacters() {
		if (characters == null) {
			characters = bool ? Constants.DECODED_BOOLEAN_TRUE
					: Constants.DECODED_BOOLEAN_FALSE;
		}
		return characters;
	}

}
