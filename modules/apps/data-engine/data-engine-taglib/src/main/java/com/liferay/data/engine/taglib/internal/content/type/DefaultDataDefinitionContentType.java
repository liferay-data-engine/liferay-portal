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

package com.liferay.data.engine.taglib.internal.content.type;

import com.liferay.data.engine.content.type.DataDefinitionContentType;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.util.Portal;

import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Paulino
 */
@Component(
	immediate = true, property = "content.type=default",
	service = DataDefinitionContentType.class
)
public class DefaultDataDefinitionContentType
	implements DataDefinitionContentType {

	@Override
	public long getClassNameId() {
		return _portal.getClassNameId(DataDefinition.class);
	}

	public class DataDefinition implements ClassedModel, Serializable {

		@Override
		public ExpandoBridge getExpandoBridge() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<?> getModelClass() {
			return DataDefinition.class;
		}

		@Override
		public String getModelClassName() {
			return DataDefinition.class.getName();
		}

		@Override
		public Serializable getPrimaryKeyObj() {
			return _dataDefinitionId;
		}

		@Override
		public void setPrimaryKeyObj(Serializable primaryKeyObj) {
			_dataDefinitionId = (long)primaryKeyObj;
		}

		private long _dataDefinitionId;

	}

	@Reference
	private Portal _portal;

}