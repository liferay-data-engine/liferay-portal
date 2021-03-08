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

import moment from 'moment/min/moment-with-locales';

export default (field, languageId) => {
	const {dataType, value} = field;

	if (dataType === 'double') {
		const {symbols} = field;

		return String(value).replace(symbols.decimalSymbol, '.');
	}

	if (
		dataType === 'date' &&
		typeof value === 'string' &&
		value.indexOf('_') === -1 &&
		value !== ''
	) {
		const {editingLanguageId} = field;

		moment.locale(editingLanguageId);

		let dateFormat = moment.localeData().longDateFormat('L');

		const date = moment(value, dateFormat).toDate();

		moment.locale(languageId);

		dateFormat = moment.localeData().longDateFormat('L');

		return moment(date).format(dateFormat);
	}

	return value;
};
