/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.bulk.rest.internal.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adolfo Pérez
 */
@XmlRootElement
public class BulkAssetEntryUpdateTagsActionModel
	extends BulkAssetEntryActionModel {

	@XmlElement
	public boolean getAppend() {
		return _append;
	}

	@XmlElement
	public String[] getToAddTagNames() {
		return _toAddTagNames;
	}

	@XmlElement
	public String[] getToRemoveTagNames() {
		return _toRemoveTagNames;
	}

	public void setAppend(boolean append) {
		_append = append;
	}

	public void setToAddTagNames(String[] toAddTagNames) {
		_toAddTagNames = toAddTagNames;
	}

	public void setToRemoveTagNames(String[] toRemoveTagNames) {
		_toRemoveTagNames = toRemoveTagNames;
	}

	private boolean _append;
	private String[] _toAddTagNames;
	private String[] _toRemoveTagNames;

}