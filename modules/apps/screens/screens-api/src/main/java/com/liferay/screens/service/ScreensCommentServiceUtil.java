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

package com.liferay.screens.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for ScreensComment. This utility wraps
 * <code>com.liferay.screens.service.impl.ScreensCommentServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author José Manuel Navarro
 * @see ScreensCommentService
 * @generated
 */
@ProviderType
public class ScreensCommentServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.screens.service.impl.ScreensCommentServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.json.JSONObject addComment(
			String className, long classPK, String body)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addComment(className, classPK, body);
	}

	public static com.liferay.portal.kernel.json.JSONObject getComment(
			long commentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getComment(commentId);
	}

	public static com.liferay.portal.kernel.json.JSONArray getComments(
			String className, long classPK, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getComments(className, classPK, start, end);
	}

	public static int getCommentsCount(String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommentsCount(className, classPK);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.json.JSONObject updateComment(
			long commentId, String body)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateComment(commentId, body);
	}

	public static ScreensCommentService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ScreensCommentService, ScreensCommentService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ScreensCommentService.class);

		ServiceTracker<ScreensCommentService, ScreensCommentService>
			serviceTracker =
				new ServiceTracker
					<ScreensCommentService, ScreensCommentService>(
						bundle.getBundleContext(), ScreensCommentService.class,
						null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}