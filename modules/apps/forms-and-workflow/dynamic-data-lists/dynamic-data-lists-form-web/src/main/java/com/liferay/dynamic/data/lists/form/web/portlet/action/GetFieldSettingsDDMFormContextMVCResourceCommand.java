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

package com.liferay.dynamic.data.lists.form.web.portlet.action;

import com.liferay.dynamic.data.lists.form.web.constants.DDLFormPortletKeys;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormFactory;
import com.liferay.dynamic.data.mapping.util.DDMFormLayoutFactory;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Locale;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDLFormPortletKeys.DYNAMIC_DATA_LISTS_FORM_ADMIN,
		"mvc.command.name=getFieldSettingsDDMFormContext"
	},
	service = MVCResourceCommand.class
)
public class GetFieldSettingsDDMFormContextMVCResourceCommand
	extends BaseMVCResourceCommand {

	protected DDMFormFieldValue createDDMFormFieldTypeSettingsDDMFormFieldValue(
		Object property, DDMFormField ddmFormFieldTypeSetting, Locale locale) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName(ddmFormFieldTypeSetting.getName());

		Value value = new UnlocalizedValue(String.valueOf(property));

		if (ddmFormFieldTypeSetting.isLocalizable()) {
			LocalizedValue localizedValue = (LocalizedValue)property;

			value.addString(locale, localizedValue.getString(locale));
		}

		ddmFormFieldValue.setValue(value);

		return ddmFormFieldValue;
	}

	protected DDMFormValues createDDMFormFieldTypeSettingsDDMFormValues(
		ResourceRequest resourceRequest, DDMFormField ddmFormField,
		DDMForm ddmFormFieldTypeSettingsDDMForm, Locale locale) {

		DDMFormValues ddmFormValues = new DDMFormValues(
			ddmFormFieldTypeSettingsDDMForm);

		ddmFormValues.addAvailableLocale(resourceRequest.getLocale());
		ddmFormValues.setDefaultLocale(resourceRequest.getLocale());

		for (DDMFormField ddmFormFieldTypeSetting :
				ddmFormFieldTypeSettingsDDMForm.getDDMFormFields()) {

			Object property = ddmFormField.getProperty(
				ddmFormFieldTypeSetting.getName());

			ddmFormValues.addDDMFormFieldValue(
				createDDMFormFieldTypeSettingsDDMFormFieldValue(
					property, ddmFormFieldTypeSetting, locale));
		}

		return ddmFormValues;
	}

	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		String definition = ParamUtil.getString(resourceRequest, "definition");
		String fieldName = ParamUtil.getString(resourceRequest, "name");

		DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(definition);

		Map<String, DDMFormField> ddmFormFieldMap = ddmForm.getDDMFormFieldsMap(
			true);

		DDMFormField ddmFormField = ddmFormFieldMap.get(fieldName);

		Class<?> ddmFormFieldTypeSettings = getDDMFormFieldTypeSettings(
			ddmFormField.getName());

		DDMForm ddmFormFieldTypeSettingsDDMForm = DDMFormFactory.create(
			ddmFormFieldTypeSettings);

		DDMFormLayout ddmFormFieldTypeSettingsDDMFormLayout =
			DDMFormLayoutFactory.create(ddmFormFieldTypeSettings);

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setHttpServletRequest(
			PortalUtil.getHttpServletRequest(resourceRequest));
		ddmFormRenderingContext.setHttpServletResponse(
			PortalUtil.getHttpServletResponse(resourceResponse));
		ddmFormRenderingContext.setContainerId("settings");
		ddmFormRenderingContext.setLocale(resourceRequest.getLocale());
		ddmFormRenderingContext.setPortletNamespace(
			resourceResponse.getNamespace());
		ddmFormRenderingContext.setDDMFormValues(
			createDDMFormFieldTypeSettingsDDMFormValues(
				resourceRequest, ddmFormField, ddmFormFieldTypeSettingsDDMForm,
				resourceRequest.getLocale()));

		ResourceURL resourceURL = resourceResponse.createResourceURL();

		resourceURL.setParameter("type", ddmFormField.getType());
		resourceURL.setResourceID("evaluateDDMFormFieldTypeSettings");

		ddmFormRenderingContext.setEvaluatorURL(resourceURL.toString());

		Map<String, Object> templateContext =
			_ddmFormTemplateContextFactory.create(
				ddmFormFieldTypeSettingsDDMForm,
				ddmFormFieldTypeSettingsDDMFormLayout, ddmFormRenderingContext);

		resourceResponse.setContentType(ContentTypes.APPLICATION_JSON);

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		PortletResponseUtil.write(
			resourceResponse, jsonSerializer.serialize(templateContext));

		resourceResponse.flushBuffer();
	}

	protected Class<?> getDDMFormFieldTypeSettings(String type) {
		DDMFormFieldType ddmFormFieldType =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldType(type);

		return ddmFormFieldType.getDDMFormFieldTypeSettings();
	}

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;

	@Reference
	private DDMFormTemplateContextFactory _ddmFormTemplateContextFactory;

	@Reference
	private JSONFactory _jsonFactory;

}