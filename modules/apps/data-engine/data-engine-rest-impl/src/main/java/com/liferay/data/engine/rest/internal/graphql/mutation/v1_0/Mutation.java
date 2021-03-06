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

package com.liferay.data.engine.rest.internal.graphql.mutation.v1_0;

import com.liferay.data.engine.rest.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v1_0.DataDefinitionResource;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import javax.annotation.Generated;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public class Mutation {

	@GraphQLField
	@GraphQLInvokeDetached
	public DataDefinition postDataDefinition(
			@GraphQLName("groupId") Long groupId,
			@GraphQLName("DataDefinition") DataDefinition dataDefinition)
		throws Exception {

		DataDefinitionResource dataDefinitionResource =
			_createDataDefinitionResource();

		return dataDefinitionResource.postDataDefinition(
			groupId, dataDefinition);
	}

	@GraphQLInvokeDetached
	public boolean deleteDataDefinition(
			@GraphQLName("data-definition-id") Long dataDefinitionId)
		throws Exception {

		DataDefinitionResource dataDefinitionResource =
			_createDataDefinitionResource();

		return dataDefinitionResource.deleteDataDefinition(dataDefinitionId);
	}

	@GraphQLInvokeDetached
	public DataDefinition putDataDefinition(
			@GraphQLName("data-definition-id") Long dataDefinitionId,
			@GraphQLName("DataDefinition") DataDefinition dataDefinition)
		throws Exception {

		DataDefinitionResource dataDefinitionResource =
			_createDataDefinitionResource();

		return dataDefinitionResource.putDataDefinition(
			dataDefinitionId, dataDefinition);
	}

	private static DataDefinitionResource _createDataDefinitionResource()
		throws Exception {

		DataDefinitionResource dataDefinitionResource =
			_dataDefinitionResourceServiceTracker.getService();

		dataDefinitionResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return dataDefinitionResource;
	}

	private static final ServiceTracker
		<DataDefinitionResource, DataDefinitionResource>
			_dataDefinitionResourceServiceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(Mutation.class);

		ServiceTracker<DataDefinitionResource, DataDefinitionResource>
			dataDefinitionResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), DataDefinitionResource.class, null);

		dataDefinitionResourceServiceTracker.open();

		_dataDefinitionResourceServiceTracker =
			dataDefinitionResourceServiceTracker;
	}

}