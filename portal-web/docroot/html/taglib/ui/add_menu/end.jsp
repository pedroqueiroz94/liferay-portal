<%--
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
--%>

<%@ include file="/html/taglib/ui/add_menu/init.jsp" %>

<%
List<AddMenuItem> addMenuItems = (List<AddMenuItem>)request.getAttribute("liferay-ui:add-menu:addMenuItems");
%>

<c:choose>
	<c:when test="<%= addMenuItems.size() == 1 %>">

		<%
		AddMenuItem menuItem = addMenuItems.get(0);
		%>

		<a class="btn btn-action btn-bottom-right btn-primary" data-placement="left" data-toggle="tooltip" href="<%= HtmlUtil.escapeAttribute(menuItem.getUrl()) %>" title="<%= HtmlUtil.escapeAttribute(menuItem.getLabel()) %>">
			<span class="icon-plus"></span>
		</a>

		<aui:script>
			$(document).ready(
				function() {
					$('[data-toggle="tooltip"]').tooltip();
				}
			);
		</aui:script>
	</c:when>
	<c:otherwise>
		<div class="btn-action-secondary btn-bottom-right dropdown">
			<button aria-expanded="false" class="btn btn-primary" data-toggle="dropdown" type="button">
				<span class="icon-plus"></span>
			</button>

			<ul class="dropdown-menu dropdown-menu-left-side-bottom">

				<%
				for (int i = 0; i < addMenuItems.size(); i++) {
					AddMenuItem menuItem = addMenuItems.get(i);
				%>

					<li>
						<a href="<%= HtmlUtil.escapeAttribute(menuItem.getUrl()) %>"><%= HtmlUtil.escape(menuItem.getLabel()) %></a>
					</li>

				<%
				}
				%>

			</ul>
		</div>
	</c:otherwise>
</c:choose>