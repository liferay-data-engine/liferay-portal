<style>

	.policy-card .policy-type {
		font-size: var(--h2-font-size);
		font-weight: var(--font-weight-bolder);
		line-height: var(--line-height-sm);
		margin-bottom: var(--spacer-1);
	}
	.policy-card .policy-name {
		font-size: var(--h3-font-size);
		line-height: var(--line-height-sm);
		margin-bottom: var(--spacer-4);
		color: var(--gray-600);
	}
</style>

<a class="card-link" href="${friendlyURLs[themeDisplay.getLanguageId()]!""}">
	<div class="policy-card product-card">
		<div class="policy-type">
			${PolicyNameFieldSet.PolicyType.getData()}
		</div>

		<div class="policy-name">
			${PolicyNameFieldSet.PolicyName.getData()}
		</div>

		<div>
			<span class="product-card-label">Insured:</span> ${PolicyOwner.getData()}
		</div>

		<div>
			<span class="product-card-label">Premium:</span> ${YearlyFee.getData()}
		</div>

		<div class="product-card-status">
			<#if StatusFieldSet.StatusImage.getData()?? && StatusFieldSet.StatusImage.getData() != "">
				<img alt="${StatusFieldSet.StatusImage.getAttribute("alt")}" data-fileentryid="${StatusFieldSet.StatusImage.getAttribute("fileEntryId")}" src="${StatusFieldSet.StatusImage.getData()}" />
			</#if>${StatusFieldSet.Status.getData()}
		</div>
	</div>
</a>