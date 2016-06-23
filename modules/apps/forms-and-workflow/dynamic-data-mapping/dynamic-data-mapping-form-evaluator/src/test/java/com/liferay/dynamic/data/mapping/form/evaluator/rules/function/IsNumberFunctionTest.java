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

package com.liferay.dynamic.data.mapping.form.evaluator.rules.function;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.rules.function.IsNumberFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.rules.DDMFormRuleEvaluatorBaseTest;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(PowerMockRunner.class)
public class IsNumberFunctionTest extends DDMFormRuleEvaluatorBaseTest {

	@Test(expected = DDMFormEvaluationException.class)
	public void testInvalidParameters() throws Exception {
		IsNumberFunction isNumberFunction = new IsNumberFunction();

		isNumberFunction.execute(
			null, null, null, null, ListUtil.fromArray(new String[0]));
	}

	@Test
	public void testIsNotNumber() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field1", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field1_instanceId", "field1", new UnlocalizedValue("value1"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, Map<String, DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap = ddmFormFieldEvaluationResults.get(
				"field1");

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResultMap.get("field1_instanceId");

		ddmFormFieldEvaluationResult.setReadOnly(false);

		List<String> parameters = ListUtil.fromArray(
			new String[] {"isNumber(field1)", "isNumber", "field1"});

		IsNumberFunction isNumberFunction = new IsNumberFunction();

		String expression = isNumberFunction.execute(
			null, null, ddmFormFieldEvaluationResults, null, parameters);

		Assert.assertEquals("FALSE", expression);
	}

	@Test
	public void testIsNumber() throws Exception {
		DDMForm ddmForm = new DDMForm();

		DDMFormField fieldDDMFormField0 = new DDMFormField("field0", "text");

		ddmForm.addDDMFormField(fieldDDMFormField0);

		DDMFormValues ddmFormValues = createDDMFormValues();

		DDMFormFieldValue fieldDDMFormFieldValue0 =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"field0_instanceId", "field0", new UnlocalizedValue("10"));

		List<DDMFormFieldValue> ddmFormFieldValues = new ArrayList<>();

		ddmFormFieldValues.add(fieldDDMFormFieldValue0);

		ddmFormValues.setDDMFormFieldValues(ddmFormFieldValues);

		Map<String, Map<String, DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		createDDMFormFieldEvaluationResult(
			fieldDDMFormField0, ddmFormValues, ddmFormFieldEvaluationResults);

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultMap = ddmFormFieldEvaluationResults.get(
				"field0");

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			ddmFormFieldEvaluationResultMap.get("field0_instanceId");

		ddmFormFieldEvaluationResult.setReadOnly(true);

		List<String> parameters = ListUtil.fromArray(
			new String[] {"isNumber(field0)", "isNumber", "field0"});

		IsNumberFunction isNumberFunction = new IsNumberFunction();

		String expression = isNumberFunction.execute(
			null, null, ddmFormFieldEvaluationResults, null, parameters);

		Assert.assertEquals("TRUE", expression);
	}

}