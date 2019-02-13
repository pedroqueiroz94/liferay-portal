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

package com.liferay.blogs.web.internal.servlet.taglib.util;

import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.web.internal.security.permission.resource.BlogsEntryPermission;
import com.liferay.blogs.web.internal.util.BlogsEntryUtil;
import com.liferay.exportimport.changeset.Changeset;
import com.liferay.exportimport.changeset.ChangesetManager;
import com.liferay.exportimport.changeset.ChangesetManagerUtil;
import com.liferay.exportimport.changeset.constants.ChangesetPortletKeys;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerRegistryUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.StagedModel;
import com.liferay.portal.kernel.model.WorkflowedModel;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.permission.GroupPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.security.PermissionsURLTag;
import com.liferay.trash.TrashHelper;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionURL;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public class BlogsEntryActionDropdownItemsProvider {

	public BlogsEntryActionDropdownItemsProvider(
		BlogsEntry blogsEntry, LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		PermissionChecker permissionChecker, ResourceBundle resourceBundle,
		TrashHelper trashHelper) {

		_blogsEntry = blogsEntry;
		_liferayPortletResponse = liferayPortletResponse;
		_permissionChecker = permissionChecker;
		_resourceBundle = resourceBundle;
		_trashHelper = trashHelper;

		_request = PortalUtil.getHttpServletRequest(liferayPortletRequest);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				if (_hasUpdatePermission()) {
					add(_getEditEntryActionConsumer());
				}

				if (_hasPermissionsPermission()) {
					add(_getPermissionsActionConsumer());
				}

				if (_hasDeletePermission()) {
					if (_isTrashEnabled()) {
						add(_getMoveEntryToTrashActionConsumer());
					}
					else {
						add(_getDeleteEntryActionConsumer());
					}
				}

				if (_isShowPublishMenuItem() &&
					_hasExportImportPortletInfoPermission()) {

					add(_getPublishToLiveEntryActionConsumer());
				}
			}
		};
	}

	/**
	 * @see com.liferay.exportimport.changeset.taglib.internal.display.context.ChangesetTaglibDisplayContext#isShowPublishMenuItem(Group, String)
	 */
	private static boolean _isShowPublishMenuItem(
		Group group, String portletId) {

		try {
			if (group.isLayout()) {
				return false;
			}

			if ((group.isStagingGroup() || group.isStagedRemotely()) &&
				group.isStagedPortlet(portletId)) {

				return true;
			}

			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see com.liferay.exportimport.changeset.taglib.internal.display.context.ChangesetTaglibDisplayContext#isShowPublishMenuItem(Group, String, String, String)
	 */
	private static boolean _isShowPublishMenuItem(
		Group group, String portletId, String className, String uuid) {

		try {
			StagedModelDataHandler stagedModelDataHandler =
				StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
					className);

			StagedModel stagedModel =
				stagedModelDataHandler.fetchStagedModelByUuidAndGroupId(
					uuid, group.getGroupId());

			if (stagedModel == null) {
				return false;
			}

			if (stagedModel instanceof WorkflowedModel) {
				WorkflowedModel workflowedModel = (WorkflowedModel)stagedModel;

				if (!ArrayUtil.contains(
						stagedModelDataHandler.getExportableStatuses(),
						workflowedModel.getStatus())) {

					return false;
				}
			}

			return _isShowPublishMenuItem(group, portletId);
		}
		catch (Exception e) {
			return false;
		}
	}

	private Consumer<DropdownItem> _getDeleteEntryActionConsumer() {
		ActionURL deleteURL = _liferayPortletResponse.createActionURL();

		deleteURL.setParameter(ActionRequest.ACTION_NAME, "/blogs/edit_entry");
		deleteURL.setParameter(Constants.CMD, Constants.DELETE);
		deleteURL.setParameter("redirect", _getRedirectURL());
		deleteURL.setParameter(
			"entryId", String.valueOf(_blogsEntry.getEntryId()));

		return dropdownItem -> {
			dropdownItem.putData("action", "delete");
			dropdownItem.putData("deleteURL", deleteURL.toString());
			dropdownItem.setLabel(LanguageUtil.get(_request, "delete"));
		};
	}

	private Consumer<DropdownItem> _getEditEntryActionConsumer() {
		return dropdownItem -> {
			dropdownItem.setHref(
				_liferayPortletResponse.createRenderURL(),
				"mvcRenderCommandName", "/blogs/edit_entry", Constants.CMD,
				Constants.UPDATE, "redirect", _getRedirectURL(), "entryId",
				_blogsEntry.getEntryId());

			dropdownItem.setIcon("edit");
			dropdownItem.setLabel(LanguageUtil.get(_resourceBundle, "edit"));
		};
	}

	private Consumer<DropdownItem> _getMoveEntryToTrashActionConsumer() {
		ActionURL moveToTrashURL = _liferayPortletResponse.createActionURL();

		moveToTrashURL.setParameter(
			ActionRequest.ACTION_NAME, "/blogs/edit_entry");
		moveToTrashURL.setParameter(Constants.CMD, Constants.MOVE_TO_TRASH);
		moveToTrashURL.setParameter("redirect", _getRedirectURL());
		moveToTrashURL.setParameter(
			"entryId", String.valueOf(_blogsEntry.getEntryId()));

		return dropdownItem -> {
			dropdownItem.putData("action", "delete");
			dropdownItem.putData("deleteURL", moveToTrashURL.toString());
			dropdownItem.setLabel(
				LanguageUtil.get(_request, "move-to-recycle-bin"));
		};
	}

	private Consumer<DropdownItem> _getPermissionsActionConsumer() {
		return dropdownItem -> {
			dropdownItem.putData("action", "permissions");
			dropdownItem.putData("permissionsURL", _getPermissionsURL());
			dropdownItem.setLabel(LanguageUtil.get(_request, "permissions"));
		};
	}

	private String _getPermissionsURL() {
		try {
			return PermissionsURLTag.doTag(
				StringPool.BLANK, BlogsEntry.class.getName(),
				BlogsEntryUtil.getDisplayTitle(_resourceBundle, _blogsEntry),
				null, String.valueOf(_blogsEntry.getEntryId()),
				LiferayWindowState.POP_UP.toString(), null, _request);
		}
		catch (Exception e) {
			return ReflectionUtil.throwException(e);
		}
	}

	private Consumer<DropdownItem> _getPublishToLiveEntryActionConsumer() {
		StagedModelDataHandler<?> stagedModelDataHandler =
			StagedModelDataHandlerRegistryUtil.getStagedModelDataHandler(
				BlogsEntry.class.getName());

		Changeset.RawBuilder rawBuilder = Changeset.createRaw();

		Changeset changeset = rawBuilder.addStagedModel(
			stagedModelDataHandler.fetchStagedModelByUuidAndGroupId(
				_blogsEntry.getUuid(), _blogsEntry.getGroupId())
		).build();

		ChangesetManager changesetManager =
			ChangesetManagerUtil.getChangesetManager();

		changesetManager.addChangeset(changeset);

		PortletURL portletURL = PortletURLFactoryUtil.create(
			_request, ChangesetPortletKeys.CHANGESET,
			PortletRequest.ACTION_PHASE);

		portletURL.setParameter(
			ActionRequest.ACTION_NAME, "exportImportChangeset");
		portletURL.setParameter(
			"mvcRenderCommandName", "exportImportChangeset");
		portletURL.setParameter("cmd", Constants.PUBLISH);
		portletURL.setParameter(
			"backURL", PortalUtil.getCurrentCompleteURL(_request));
		portletURL.setParameter(
			"groupId", String.valueOf(_blogsEntry.getGroupId()));
		portletURL.setParameter("changesetUuid", changeset.getUuid());
		portletURL.setParameter("portletId", BlogsPortletKeys.BLOGS_ADMIN);

		return dropdownItem -> {
			dropdownItem.putData("action", "publishToLive");
			dropdownItem.putData("permissionsURL", portletURL.toString());
			dropdownItem.setLabel("publish-to-live");
		};
	}

	private String _getRedirectURL() {
		PortletURL redirectURL = _liferayPortletResponse.createRenderURL();

		redirectURL.setParameter("mvcRenderCommandName", "/blogs/view");

		return redirectURL.toString();
	}

	private boolean _hasDeletePermission() {
		try {
			return BlogsEntryPermission.contains(
				_permissionChecker, _blogsEntry, ActionKeys.DELETE);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _hasExportImportPortletInfoPermission() {
		try {
			return GroupPermissionUtil.contains(
				_permissionChecker, _blogsEntry.getGroupId(),
				ActionKeys.EXPORT_IMPORT_PORTLET_INFO);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _hasPermissionsPermission() {
		try {
			return BlogsEntryPermission.contains(
				_permissionChecker, _blogsEntry, ActionKeys.PERMISSIONS);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _hasUpdatePermission() {
		try {
			return BlogsEntryPermission.contains(
				_permissionChecker, _blogsEntry, ActionKeys.UPDATE);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _isShowPublishMenuItem() {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return _isShowPublishMenuItem(
			themeDisplay.getScopeGroup(), BlogsPortletKeys.BLOGS_ADMIN,
			BlogsEntry.class.getName(), _blogsEntry.getUuid());
	}

	private boolean _isTrashEnabled() {
		try {
			return _trashHelper.isTrashEnabled(
				PortalUtil.getScopeGroupId(_request));
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private final BlogsEntry _blogsEntry;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final PermissionChecker _permissionChecker;
	private final HttpServletRequest _request;
	private final ResourceBundle _resourceBundle;
	private final TrashHelper _trashHelper;

}