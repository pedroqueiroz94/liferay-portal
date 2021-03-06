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

package com.liferay.headless.foundation.internal.resource.v1_0;

import com.liferay.headless.foundation.dto.v1_0.Segment;
import com.liferay.headless.foundation.resource.v1_0.SegmentResource;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.segments.context.Context;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.provider.SegmentsEntryProvider;
import com.liferay.segments.service.SegmentsEntryService;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/segment.properties",
	scope = ServiceScope.PROTOTYPE, service = SegmentResource.class
)
public class SegmentResourceImpl extends BaseSegmentResourceImpl {

	@Override
	public Page<Segment> getUserAccountUserSegmentsPage(
			Long userId, Pagination pagination)
		throws Exception {

		User user = _userService.getUserById(userId);

		long[] segmentsEntryIds = _segmentsEntryProvider.getSegmentsEntryIds(
			user.getModelClassName(), user.getPrimaryKey(),
			_createSegmentsContext());

		return Page.of(
			transformToList(
				ArrayUtil.toArray(segmentsEntryIds), this::_toSegment),
			pagination, segmentsEntryIds.length);
	}

	private Context _createSegmentsContext() {
		Context context = new Context();

		MultivaluedMap<String, String> multivaluedMap =
			_httpHeaders.getRequestHeaders();

		for (Map.Entry<String, List<String>> entry :
				multivaluedMap.entrySet()) {

			String key = StringUtil.toLowerCase(entry.getKey());

			List<String> values = entry.getValue();

			String value = values.get(0);

			if (key.startsWith("x-")) {
				context.put(
					CamelCaseUtil.toCamelCase(key.replace("x-", "")), value);
			}
			else if (key.equals("accept-language")) {
				context.put(Context.LANGUAGE_ID, value.replace("-", "_"));
			}
			else if (key.equals("host")) {
				context.put(Context.URL, value);
			}
			else if (key.equals("referer")) {
				context.put(Context.REFERRER_URL, value);
			}
			else if (key.equals("user-agent")) {
				context.put(Context.USER_AGENT, value);
			}
			else {
				context.put(key, value);
			}
		}

		context.put(Context.LOCAL_DATE, LocalDate.from(ZonedDateTime.now()));

		return context;
	}

	private Segment _toSegment(long segmentsEntryId) throws Exception {
		SegmentsEntry segmentsEntry = _segmentsEntryService.getSegmentsEntry(
			segmentsEntryId);

		return new Segment() {
			{
				active = segmentsEntry.isActive();
				criteria = segmentsEntry.getCriteria();
				dateCreated = segmentsEntry.getCreateDate();
				dateModified = segmentsEntry.getModifiedDate();
				id = segmentsEntry.getSegmentsEntryId();
				name = segmentsEntry.getName(
					segmentsEntry.getDefaultLanguageId());
				source = segmentsEntry.getSource();
			}
		};
	}

	@javax.ws.rs.core.Context
	private HttpHeaders _httpHeaders;

	@Reference
	private SegmentsEntryProvider _segmentsEntryProvider;

	@Reference
	private SegmentsEntryService _segmentsEntryService;

	@Reference
	private UserService _userService;

}