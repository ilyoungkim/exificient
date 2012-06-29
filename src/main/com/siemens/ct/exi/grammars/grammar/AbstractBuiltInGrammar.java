/*
 * Copyright (C) 2007-2011 Siemens AG
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

package com.siemens.ct.exi.grammars.grammar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.grammars.event.Attribute;
import com.siemens.ct.exi.grammars.event.Event;
import com.siemens.ct.exi.grammars.event.EventType;
import com.siemens.ct.exi.grammars.event.StartElement;
import com.siemens.ct.exi.grammars.production.Production;
import com.siemens.ct.exi.grammars.production.SchemaLessProduction;
import com.siemens.ct.exi.util.MethodsBag;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */

public abstract class AbstractBuiltInGrammar extends AbstractGrammar implements
		BuiltInGrammar {

	private static final long serialVersionUID = -4412097592336436189L;

	protected List<Production> containers;
	protected int eventCount;

	public AbstractBuiltInGrammar() {
		super();
		containers = new ArrayList<Production>();
		eventCount = 0;
	}

	public final boolean isSchemaInformed() {
		return false;
	}

	public boolean hasSecondOrThirdLevel(FidelityOptions fidelityOptions) {
		return true;
	}

	public Grammar getTypeEmpty() {
		return this;
	}

	public int get1stLevelEventCodeLength(FidelityOptions fidelityOptions) {
		return (hasSecondOrThirdLevel(fidelityOptions) ? MethodsBag
				.getCodingLength(eventCount + 1) : MethodsBag
				.getCodingLength(eventCount));
	}

	public int getNumberOfEvents() {
		return containers.size();
	}

	/*
	 * a leading rule for performance reason is added to the tail
	 */
	public final void addProduction(Event event, Grammar rule) {

		containers.add(new SchemaLessProduction(this, rule, event,
				getNumberOfEvents()));
		// TODO pre-calculate count for log2
		eventCount = containers.size();
	}

	protected boolean contains(Event event) {
		Iterator<Production> iter = containers.iterator();

		while (iter.hasNext()) {
			if (iter.next().getEvent().equals(event)) {
				return true;
			}
		}

		return false;
	}

	public int getNumberOfDeclaredAttributes() {
		throw new RuntimeException(
				"Schema-related attribute dealing in schema-less case");
	}

	public int getLeastAttributeEventCode() {
		throw new RuntimeException(
				"Schema-related attribute dealing in schema-less case");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getLabel() + "//" + "\t");

		sb.append("[");
		for (int ec = 0; ec < this.getNumberOfEvents(); ec++) {
			sb.append("," + lookFor(ec).getEvent());
		}
		sb.append("]");

		return sb.toString();
	}

	public Production lookForEvent(EventType eventType) {
		for (Production ei : containers) {
			if (ei.getEvent().isEventType(eventType)) {
				return ei;
			}
		}
		return null; // not found
	}

	public Production lookForStartElement(String namespaceURI,
			String localName) {
		for (Production ei : containers) {
			if (ei.getEvent().isEventType(EventType.START_ELEMENT)
					&& checkQualifiedName(((StartElement) ei.getEvent()).getQName(),
							namespaceURI, localName)) {
				return ei;
			}
		}
		return null; // not found
	}

	public Production lookForStartElementNS(String namespaceURI) {
		return null; // not found
	}

	public Production lookForAttribute(String namespaceURI,
			String localName) {
		for (Production ei : containers) {
			if (ei.getEvent().isEventType(EventType.ATTRIBUTE)
					&& checkQualifiedName(((Attribute) ei.getEvent()).getQName(),
							namespaceURI, localName)) {
				return ei;
			}
		}
		return null; // not found
	}

	public Production lookForAttributeNS(String namespaceURI) {
		return null; // not found
	}

	// for decoder
	public Production lookFor(int eventCode) {
		assert (eventCode >= 0 && eventCode < containers.size());
		return containers.get(getNumberOfEvents() - 1 - eventCode);
	}

}