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

package com.liferay.site.memberships.web.internal.servlet.taglib.util;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.function.Consumer;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class OrganizationActionDropdownItemsProvider {

	public OrganizationActionDropdownItemsProvider(
		Organization organization, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_organization = organization;
		_renderResponse = renderResponse;

		_request = PortalUtil.getHttpServletRequest(renderRequest);

		_themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getActionDropdownItems() throws Exception {
		return new DropdownItemList() {
			{
				if (GroupPermissionUtil.contains(
						_themeDisplay.getPermissionChecker(),
						_themeDisplay.getScopeGroupId(),
						ActionKeys.ASSIGN_MEMBERS)) {

					add(_getDeleteGroupOrganizationsActionConsumer());
				}
			}
		};
	}

	private Consumer<DropdownItem>
		_getDeleteGroupOrganizationsActionConsumer() {

		PortletURL deleteGroupOrganizationsURL =
			_renderResponse.createActionURL();

		deleteGroupOrganizationsURL.setParameter(
			ActionRequest.ACTION_NAME, "deleteGroupOrganizations");
		deleteGroupOrganizationsURL.setParameter(
			"redirect", _themeDisplay.getURLCurrent());
		deleteGroupOrganizationsURL.setParameter(
			"groupId", String.valueOf(_themeDisplay.getScopeGroupId()));
		deleteGroupOrganizationsURL.setParameter(
			"removeOrganizationId",
			String.valueOf(_organization.getOrganizationId()));

		return dropdownItem -> {
			dropdownItem.putData("action", "deleteGroupOrganizations");
			dropdownItem.putData(
				"deleteGroupOrganizationsURL",
				deleteGroupOrganizationsURL.toString());
			dropdownItem.setLabel(
				LanguageUtil.get(_request, "remove-membership"));
		};
	}

	private final Organization _organization;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;
	private final ThemeDisplay _themeDisplay;

}