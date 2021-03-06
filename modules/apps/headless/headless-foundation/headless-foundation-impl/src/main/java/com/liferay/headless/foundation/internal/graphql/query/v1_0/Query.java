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

package com.liferay.headless.foundation.internal.graphql.query.v1_0;

import com.liferay.headless.foundation.dto.v1_0.Category;
import com.liferay.headless.foundation.dto.v1_0.Email;
import com.liferay.headless.foundation.dto.v1_0.Keyword;
import com.liferay.headless.foundation.dto.v1_0.Organization;
import com.liferay.headless.foundation.dto.v1_0.Phone;
import com.liferay.headless.foundation.dto.v1_0.PostalAddress;
import com.liferay.headless.foundation.dto.v1_0.Role;
import com.liferay.headless.foundation.dto.v1_0.Segment;
import com.liferay.headless.foundation.dto.v1_0.SegmentUser;
import com.liferay.headless.foundation.dto.v1_0.UserAccount;
import com.liferay.headless.foundation.dto.v1_0.Vocabulary;
import com.liferay.headless.foundation.dto.v1_0.WebUrl;
import com.liferay.headless.foundation.resource.v1_0.CategoryResource;
import com.liferay.headless.foundation.resource.v1_0.EmailResource;
import com.liferay.headless.foundation.resource.v1_0.KeywordResource;
import com.liferay.headless.foundation.resource.v1_0.OrganizationResource;
import com.liferay.headless.foundation.resource.v1_0.PhoneResource;
import com.liferay.headless.foundation.resource.v1_0.PostalAddressResource;
import com.liferay.headless.foundation.resource.v1_0.RoleResource;
import com.liferay.headless.foundation.resource.v1_0.SegmentResource;
import com.liferay.headless.foundation.resource.v1_0.SegmentUserResource;
import com.liferay.headless.foundation.resource.v1_0.UserAccountResource;
import com.liferay.headless.foundation.resource.v1_0.VocabularyResource;
import com.liferay.headless.foundation.resource.v1_0.WebUrlResource;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import java.util.Collection;

import javax.annotation.Generated;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class Query {

	@GraphQLField
	@GraphQLInvokeDetached
	public Category getCategory(@GraphQLName("category-id") Long categoryId)
		throws Exception {

		CategoryResource categoryResource = _createCategoryResource();

		return categoryResource.getCategory(categoryId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Category> getCategoryCategoriesPage(
			@GraphQLName("category-id") Long categoryId,
			@GraphQLName("filter") Filter filter,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page, @GraphQLName("Sort[]") Sort[] sorts)
		throws Exception {

		CategoryResource categoryResource = _createCategoryResource();

		Page paginationPage = categoryResource.getCategoryCategoriesPage(
			categoryId, filter, Pagination.of(pageSize, page), sorts);

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Category> getVocabularyCategoriesPage(
			@GraphQLName("vocabulary-id") Long vocabularyId,
			@GraphQLName("filter") Filter filter,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page, @GraphQLName("Sort[]") Sort[] sorts)
		throws Exception {

		CategoryResource categoryResource = _createCategoryResource();

		Page paginationPage = categoryResource.getVocabularyCategoriesPage(
			vocabularyId, filter, Pagination.of(pageSize, page), sorts);

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Email> getEmailsByClassNameClassPK(
			@GraphQLName("classNameClassPK")
				com.liferay.portal.vulcan.identifier.ClassNameClassPK
					classNameClassPK,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		EmailResource emailResource = _createEmailResource();

		Page paginationPage = emailResource.getEmailsByClassNameClassPK(
			classNameClassPK, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Email getEmail(@GraphQLName("email-id") Long emailId)
		throws Exception {

		EmailResource emailResource = _createEmailResource();

		return emailResource.getEmail(emailId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Keyword> getContentSpaceKeywordsPage(
			@GraphQLName("content-space-id") Long contentSpaceId,
			@GraphQLName("filter") Filter filter,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page, @GraphQLName("Sort[]") Sort[] sorts)
		throws Exception {

		KeywordResource keywordResource = _createKeywordResource();

		Page paginationPage = keywordResource.getContentSpaceKeywordsPage(
			contentSpaceId, filter, Pagination.of(pageSize, page), sorts);

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Keyword getKeyword(@GraphQLName("keyword-id") Long keywordId)
		throws Exception {

		KeywordResource keywordResource = _createKeywordResource();

		return keywordResource.getKeyword(keywordId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Organization> getMyUserAccountOrganizationsPage(
			@GraphQLName("my-user-account-id") Long myUserAccountId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		OrganizationResource organizationResource =
			_createOrganizationResource();

		Page paginationPage =
			organizationResource.getMyUserAccountOrganizationsPage(
				myUserAccountId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Organization> getOrganizationsPage(
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		OrganizationResource organizationResource =
			_createOrganizationResource();

		Page paginationPage = organizationResource.getOrganizationsPage(
			Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Organization getOrganization(
			@GraphQLName("organization-id") Long organizationId)
		throws Exception {

		OrganizationResource organizationResource =
			_createOrganizationResource();

		return organizationResource.getOrganization(organizationId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Organization> getOrganizationOrganizationsPage(
			@GraphQLName("organization-id") Long organizationId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		OrganizationResource organizationResource =
			_createOrganizationResource();

		Page paginationPage =
			organizationResource.getOrganizationOrganizationsPage(
				organizationId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Organization> getUserAccountOrganizationsPage(
			@GraphQLName("user-account-id") Long userAccountId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		OrganizationResource organizationResource =
			_createOrganizationResource();

		Page paginationPage =
			organizationResource.getUserAccountOrganizationsPage(
				userAccountId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Phone> getPhonesPage(
			@GraphQLName("generic-parent-id") Object genericParentId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		PhoneResource phoneResource = _createPhoneResource();

		Page paginationPage = phoneResource.getPhonesPage(
			genericParentId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Phone getPhone(@GraphQLName("phone-id") Long phoneId)
		throws Exception {

		PhoneResource phoneResource = _createPhoneResource();

		return phoneResource.getPhone(phoneId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<PostalAddress> getPostalAddressesPage(
			@GraphQLName("generic-parent-id") Object genericParentId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		PostalAddressResource postalAddressResource =
			_createPostalAddressResource();

		Page paginationPage = postalAddressResource.getPostalAddressesPage(
			genericParentId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public PostalAddress getPostalAddress(
			@GraphQLName("postal-address-id") Long postalAddressId)
		throws Exception {

		PostalAddressResource postalAddressResource =
			_createPostalAddressResource();

		return postalAddressResource.getPostalAddress(postalAddressId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Role> getMyUserAccountRolesPage(
			@GraphQLName("my-user-account-id") Long myUserAccountId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		RoleResource roleResource = _createRoleResource();

		Page paginationPage = roleResource.getMyUserAccountRolesPage(
			myUserAccountId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Role> getRolesPage(
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		RoleResource roleResource = _createRoleResource();

		Page paginationPage = roleResource.getRolesPage(
			Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Role getRole(@GraphQLName("role-id") Long roleId) throws Exception {
		RoleResource roleResource = _createRoleResource();

		return roleResource.getRole(roleId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Role> getUserAccountRolesPage(
			@GraphQLName("user-account-id") Long userAccountId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		RoleResource roleResource = _createRoleResource();

		Page paginationPage = roleResource.getUserAccountRolesPage(
			userAccountId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Segment> getUserAccountUserSegmentsPage(
			@GraphQLName("user-id") Long userId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		SegmentResource segmentResource = _createSegmentResource();

		Page paginationPage = segmentResource.getUserAccountUserSegmentsPage(
			userId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<SegmentUser> getSegmentUserAccountsPage(
			@GraphQLName("segment-id") Long segmentId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		SegmentUserResource segmentUserResource = _createSegmentUserResource();

		Page paginationPage = segmentUserResource.getSegmentUserAccountsPage(
			segmentId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public UserAccount getMyUserAccount(
			@GraphQLName("my-user-account-id") Long myUserAccountId)
		throws Exception {

		UserAccountResource userAccountResource = _createUserAccountResource();

		return userAccountResource.getMyUserAccount(myUserAccountId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<UserAccount> getOrganizationUserAccountsPage(
			@GraphQLName("organization-id") Long organizationId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		UserAccountResource userAccountResource = _createUserAccountResource();

		Page paginationPage =
			userAccountResource.getOrganizationUserAccountsPage(
				organizationId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<UserAccount> getUserAccountsPage(
			@GraphQLName("fullnamequery") String fullnamequery,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		UserAccountResource userAccountResource = _createUserAccountResource();

		Page paginationPage = userAccountResource.getUserAccountsPage(
			fullnamequery, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public UserAccount getUserAccount(
			@GraphQLName("user-account-id") Long userAccountId)
		throws Exception {

		UserAccountResource userAccountResource = _createUserAccountResource();

		return userAccountResource.getUserAccount(userAccountId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<UserAccount> getWebSiteUserAccountsPage(
			@GraphQLName("web-site-id") Long webSiteId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		UserAccountResource userAccountResource = _createUserAccountResource();

		Page paginationPage = userAccountResource.getWebSiteUserAccountsPage(
			webSiteId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Vocabulary> getContentSpaceVocabulariesPage(
			@GraphQLName("content-space-id") Long contentSpaceId,
			@GraphQLName("filter") Filter filter,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page, @GraphQLName("Sort[]") Sort[] sorts)
		throws Exception {

		VocabularyResource vocabularyResource = _createVocabularyResource();

		Page paginationPage =
			vocabularyResource.getContentSpaceVocabulariesPage(
				contentSpaceId, filter, Pagination.of(pageSize, page), sorts);

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Vocabulary getVocabulary(
			@GraphQLName("vocabulary-id") Long vocabularyId)
		throws Exception {

		VocabularyResource vocabularyResource = _createVocabularyResource();

		return vocabularyResource.getVocabulary(vocabularyId);
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<WebUrl> getWebUrlsPage(
			@GraphQLName("generic-parent-id") Object genericParentId,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page)
		throws Exception {

		WebUrlResource webUrlResource = _createWebUrlResource();

		Page paginationPage = webUrlResource.getWebUrlsPage(
			genericParentId, Pagination.of(pageSize, page));

		return paginationPage.getItems();
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public WebUrl getWebUrl(@GraphQLName("web-url-id") Long webUrlId)
		throws Exception {

		WebUrlResource webUrlResource = _createWebUrlResource();

		return webUrlResource.getWebUrl(webUrlId);
	}

	private static CategoryResource _createCategoryResource() throws Exception {
		CategoryResource categoryResource =
			_categoryResourceServiceTracker.getService();

		categoryResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return categoryResource;
	}

	private static final ServiceTracker<CategoryResource, CategoryResource>
		_categoryResourceServiceTracker;

	private static EmailResource _createEmailResource() throws Exception {
		EmailResource emailResource = _emailResourceServiceTracker.getService();

		emailResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return emailResource;
	}

	private static final ServiceTracker<EmailResource, EmailResource>
		_emailResourceServiceTracker;

	private static KeywordResource _createKeywordResource() throws Exception {
		KeywordResource keywordResource =
			_keywordResourceServiceTracker.getService();

		keywordResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return keywordResource;
	}

	private static final ServiceTracker<KeywordResource, KeywordResource>
		_keywordResourceServiceTracker;

	private static OrganizationResource _createOrganizationResource()
		throws Exception {

		OrganizationResource organizationResource =
			_organizationResourceServiceTracker.getService();

		organizationResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return organizationResource;
	}

	private static final ServiceTracker
		<OrganizationResource, OrganizationResource>
			_organizationResourceServiceTracker;

	private static PhoneResource _createPhoneResource() throws Exception {
		PhoneResource phoneResource = _phoneResourceServiceTracker.getService();

		phoneResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return phoneResource;
	}

	private static final ServiceTracker<PhoneResource, PhoneResource>
		_phoneResourceServiceTracker;

	private static PostalAddressResource _createPostalAddressResource()
		throws Exception {

		PostalAddressResource postalAddressResource =
			_postalAddressResourceServiceTracker.getService();

		postalAddressResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return postalAddressResource;
	}

	private static final ServiceTracker
		<PostalAddressResource, PostalAddressResource>
			_postalAddressResourceServiceTracker;

	private static RoleResource _createRoleResource() throws Exception {
		RoleResource roleResource = _roleResourceServiceTracker.getService();

		roleResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return roleResource;
	}

	private static final ServiceTracker<RoleResource, RoleResource>
		_roleResourceServiceTracker;

	private static SegmentResource _createSegmentResource() throws Exception {
		SegmentResource segmentResource =
			_segmentResourceServiceTracker.getService();

		segmentResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return segmentResource;
	}

	private static final ServiceTracker<SegmentResource, SegmentResource>
		_segmentResourceServiceTracker;

	private static SegmentUserResource _createSegmentUserResource()
		throws Exception {

		SegmentUserResource segmentUserResource =
			_segmentUserResourceServiceTracker.getService();

		segmentUserResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return segmentUserResource;
	}

	private static final ServiceTracker
		<SegmentUserResource, SegmentUserResource>
			_segmentUserResourceServiceTracker;

	private static UserAccountResource _createUserAccountResource()
		throws Exception {

		UserAccountResource userAccountResource =
			_userAccountResourceServiceTracker.getService();

		userAccountResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return userAccountResource;
	}

	private static final ServiceTracker
		<UserAccountResource, UserAccountResource>
			_userAccountResourceServiceTracker;

	private static VocabularyResource _createVocabularyResource()
		throws Exception {

		VocabularyResource vocabularyResource =
			_vocabularyResourceServiceTracker.getService();

		vocabularyResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return vocabularyResource;
	}

	private static final ServiceTracker<VocabularyResource, VocabularyResource>
		_vocabularyResourceServiceTracker;

	private static WebUrlResource _createWebUrlResource() throws Exception {
		WebUrlResource webUrlResource =
			_webUrlResourceServiceTracker.getService();

		webUrlResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));

		return webUrlResource;
	}

	private static final ServiceTracker<WebUrlResource, WebUrlResource>
		_webUrlResourceServiceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(Query.class);

		ServiceTracker<CategoryResource, CategoryResource>
			categoryResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), CategoryResource.class, null);

		categoryResourceServiceTracker.open();

		_categoryResourceServiceTracker = categoryResourceServiceTracker;
		ServiceTracker<EmailResource, EmailResource>
			emailResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), EmailResource.class, null);

		emailResourceServiceTracker.open();

		_emailResourceServiceTracker = emailResourceServiceTracker;
		ServiceTracker<KeywordResource, KeywordResource>
			keywordResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), KeywordResource.class, null);

		keywordResourceServiceTracker.open();

		_keywordResourceServiceTracker = keywordResourceServiceTracker;
		ServiceTracker<OrganizationResource, OrganizationResource>
			organizationResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), OrganizationResource.class, null);

		organizationResourceServiceTracker.open();

		_organizationResourceServiceTracker =
			organizationResourceServiceTracker;
		ServiceTracker<PhoneResource, PhoneResource>
			phoneResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), PhoneResource.class, null);

		phoneResourceServiceTracker.open();

		_phoneResourceServiceTracker = phoneResourceServiceTracker;
		ServiceTracker<PostalAddressResource, PostalAddressResource>
			postalAddressResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), PostalAddressResource.class, null);

		postalAddressResourceServiceTracker.open();

		_postalAddressResourceServiceTracker =
			postalAddressResourceServiceTracker;
		ServiceTracker<RoleResource, RoleResource> roleResourceServiceTracker =
			new ServiceTracker<>(
				bundle.getBundleContext(), RoleResource.class, null);

		roleResourceServiceTracker.open();

		_roleResourceServiceTracker = roleResourceServiceTracker;
		ServiceTracker<SegmentResource, SegmentResource>
			segmentResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), SegmentResource.class, null);

		segmentResourceServiceTracker.open();

		_segmentResourceServiceTracker = segmentResourceServiceTracker;
		ServiceTracker<SegmentUserResource, SegmentUserResource>
			segmentUserResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), SegmentUserResource.class, null);

		segmentUserResourceServiceTracker.open();

		_segmentUserResourceServiceTracker = segmentUserResourceServiceTracker;
		ServiceTracker<UserAccountResource, UserAccountResource>
			userAccountResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), UserAccountResource.class, null);

		userAccountResourceServiceTracker.open();

		_userAccountResourceServiceTracker = userAccountResourceServiceTracker;
		ServiceTracker<VocabularyResource, VocabularyResource>
			vocabularyResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), VocabularyResource.class, null);

		vocabularyResourceServiceTracker.open();

		_vocabularyResourceServiceTracker = vocabularyResourceServiceTracker;
		ServiceTracker<WebUrlResource, WebUrlResource>
			webUrlResourceServiceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), WebUrlResource.class, null);

		webUrlResourceServiceTracker.open();

		_webUrlResourceServiceTracker = webUrlResourceServiceTracker;
	}

}