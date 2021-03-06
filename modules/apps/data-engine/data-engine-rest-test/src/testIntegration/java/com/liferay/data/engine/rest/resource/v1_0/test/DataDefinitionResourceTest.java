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

package com.liferay.data.engine.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.rest.dto.v1_0.DataDefinition;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * @author Jeyvison Nascimento
 */
@Ignore
@RunWith(Arquillian.class)
public class DataDefinitionResourceTest
	extends BaseDataDefinitionResourceTestCase {

	protected void assertValid(DataDefinition dataDefinition) {
		boolean valid = false;

		if ((dataDefinition.getDateCreated() != null) &&
			(dataDefinition.getDateModified() != null) &&
			Objects.equals(
				dataDefinition.getGroupId(), testGroup.getGroupId()) &&
			(dataDefinition.getId() != null)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	@Override
	protected boolean equals(
		DataDefinition dataDefinition1, DataDefinition dataDefinition2) {

		if (Objects.equals(
				dataDefinition1.getDateCreated(),
				dataDefinition2.getDateCreated()) &&
			Objects.equals(dataDefinition1.getId(), dataDefinition2.getId())) {

			return true;
		}

		return false;
	}

	protected DataDefinition randomDataDefinition() {
		return new DataDefinition() {
			{
			}
		};
	}

	protected DataDefinition testDeleteDataDefinition_addDataDefinition()
		throws Exception {

		return invokePostDataDefinition(
			testGroup.getGroupId(), randomDataDefinition());
	}

	protected DataDefinition testGetDataDefinition_addDataDefinition()
		throws Exception {

		return invokePostDataDefinition(
			testGroup.getGroupId(), randomDataDefinition());
	}

	protected DataDefinition testGetDataDefinitionsPage_addDataDefinition(
			Long groupId, DataDefinition dataDefinition)
		throws Exception {

		return invokePostDataDefinition(groupId, dataDefinition);
	}

	protected Long testGetDataDefinitionsPage_getGroupId() throws Exception {
		return testGroup.getGroupId();
	}

	protected DataDefinition testPostDataDefinition_addDataDefinition(
			DataDefinition dataDefinition)
		throws Exception {

		return invokePostDataDefinition(testGroup.getGroupId(), dataDefinition);
	}

	protected DataDefinition testPutDataDefinition_addDataDefinition()
		throws Exception {

		return invokePostDataDefinition(
			testGroup.getGroupId(), randomDataDefinition());
	}

}