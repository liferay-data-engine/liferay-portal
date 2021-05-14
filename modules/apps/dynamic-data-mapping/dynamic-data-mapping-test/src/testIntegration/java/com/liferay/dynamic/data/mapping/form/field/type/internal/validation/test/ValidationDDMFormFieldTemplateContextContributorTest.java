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

package com.liferay.dynamic.data.mapping.form.field.type.internal.validation.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

/**
 * @author Rodrigo Paulino
 */
@RunWith(Arquillian.class)
public class ValidationDDMFormFieldTemplateContextContributorTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_ddmFormField = DDMFormTestUtil.createValidationDDMFormField(
			"string", StringUtil.randomString(), true,
			StringUtil.randomString(), false, false, null);

		_ddmFormFieldRenderingContext = new DDMFormFieldRenderingContext();

		_ddmFormFieldRenderingContext.setLocale(LocaleUtil.US);
		_ddmFormFieldRenderingContext.setValue(StringUtil.randomString());

		_parameters =
			(HashMap<String, Object>)
				_ddmFormFieldTemplateContextContributor.getParameters(
					_ddmFormField, _ddmFormFieldRenderingContext);
	}

	@Test
	public void testGetParameters() {
		Assert.assertTrue(_parameters.containsKey("validations"));
		Assert.assertTrue(_parameters.containsKey("value"));
	}

	@Test
	public void testValidationsParameter() {
		HashMap<String, Object> actualValidationsMap =
			(HashMap<String, Object>)_parameters.get("validations");

		Assert.assertEquals(
			actualValidationsMap.toString(), 2, actualValidationsMap.size());
		Assert.assertTrue(actualValidationsMap.containsKey("numeric"));
		Assert.assertTrue(actualValidationsMap.containsKey("string"));

		_assertValidations(
			actualValidationsMap, "numeric",
			HashMapBuilder.put(
				"eq",
				_getValidation(
					"/^(.+)==(\\d+\\.?\\d*)?$/", "{name} == {parameter}")
			).put(
				"gt",
				_getValidation(
					"/^(.+)<(\\d+\\.?\\d*)?$/", "{name} < {parameter}")
			).put(
				"gteq",
				_getValidation(
					"/^(.+)<=(\\d+\\.?\\d*)?$/", "{name} <= {parameter}")
			).put(
				"lt",
				_getValidation(
					"/^(.+)>(\\d+\\.?\\d*)?$/", "{name} > {parameter}")
			).put(
				"lteq",
				_getValidation(
					"/^(.+)>=(\\d+\\.?\\d*)?$/", "{name} >= {parameter}")
			).put(
				"neq",
				_getValidation(
					"/^(.+)!=(\\d+\\.?\\d*)?$/", "{name} != {parameter}")
			).build());

		_assertValidations(
			actualValidationsMap, "string",
			HashMapBuilder.put(
				"contains",
				_getValidation(
					"/^contains\\((.+), \"(.*)\"\\)$/",
					"contains({name}, \"{parameter}\")")
			).put(
				"email",
				_getValidation(
					"/^isEmailAddress\\((.+)\\)$/", "isEmailAddress({name})")
			).put(
				"notContains",
				_getValidation(
					"/^NOT\\(contains\\((.+), \"(.*)\"\\)\\)$/",
					"NOT(contains({name}, \"{parameter}\"))")
			).put(
				"regularExpression",
				_getValidation(
					"/^match\\((.+), \"(.*)\"\\)$/",
					"match({name}, \"{parameter}\")")
			).put(
				"url", _getValidation("/^isURL\\((.+)\\)$/", "isURL({name})")
			).build());
	}

	@Test
	public void testValueParameter() {
		HashMap<String, Object> value =
			(HashMap<String, Object>)_parameters.get("value");

		Assert.assertEquals(value.toString(), 3, value.size());
		Assert.assertTrue(value.containsKey("errorMessage"));
		Assert.assertTrue(value.containsKey("expression"));
		Assert.assertTrue(value.containsKey("parameter"));
	}

	private void _assertValidations(
		HashMap<String, Object> actualValidationsMap, String dataType,
		HashMap<String, HashMap<String, String>> expectedValidationsMap) {

		Object[] actualValidations = (Object[])actualValidationsMap.get(
			dataType);

		Assert.assertEquals(
			Arrays.toString(actualValidations), expectedValidationsMap.size(),
			actualValidations.length);

		HashMap<String, Object> reducedActualValidationsMap = new HashMap<>();

		for (Object actualValidation : actualValidations) {
			HashMap<String, Object> actualValidationPropertiesMap =
				(HashMap<String, Object>)actualValidation;

			Assert.assertTrue(
				actualValidationPropertiesMap.containsKey("label"));
			Assert.assertTrue(
				actualValidationPropertiesMap.containsKey("name"));
			Assert.assertTrue(
				actualValidationPropertiesMap.containsKey("parameterMessage"));
			Assert.assertTrue(
				actualValidationPropertiesMap.containsKey("regex"));
			Assert.assertTrue(
				actualValidationPropertiesMap.containsKey("template"));

			reducedActualValidationsMap.put(
				(String)actualValidationPropertiesMap.get("name"),
				actualValidationPropertiesMap);
		}

		for (Map.Entry<String, HashMap<String, String>> entry :
				expectedValidationsMap.entrySet()) {

			Assert.assertTrue(
				reducedActualValidationsMap.containsKey(entry.getKey()));

			HashMap<String, String> expectedValidationPropertiesMap =
				entry.getValue();

			HashMap<String, Object> actualValidationPropertiesMap =
				(HashMap<String, Object>)reducedActualValidationsMap.get(
					entry.getKey());

			Assert.assertEquals(
				expectedValidationPropertiesMap.get("regex"),
				actualValidationPropertiesMap.get("regex"));
			Assert.assertEquals(
				expectedValidationPropertiesMap.get("template"),
				actualValidationPropertiesMap.get("template"));
		}
	}

	private HashMap<String, String> _getValidation(
		String regex, String template) {

		return HashMapBuilder.put(
			"regex", regex
		).put(
			"template", template
		).build();
	}

	private DDMFormField _ddmFormField;
	private DDMFormFieldRenderingContext _ddmFormFieldRenderingContext;

	@Inject(
		filter = "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.VALIDATION
	)
	private DDMFormFieldTemplateContextContributor
		_ddmFormFieldTemplateContextContributor;

	private HashMap<String, Object> _parameters;

}