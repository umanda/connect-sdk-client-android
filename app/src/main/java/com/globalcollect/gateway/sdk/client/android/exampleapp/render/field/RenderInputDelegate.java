package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.FormElement.ListType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Delegate which handles the rendering of fields 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderInputDelegate {
	

	// Map containing all custom renderers per FormElement type
	private HashMap<ListType, RenderInputFieldInterface> customRenderers;
	
	private RenderInputFieldHelper renderField;
	
	
	/**
	 * Constructor 
	 * @param parentView, the ViewGroup to which all the rendered field are added
	 */
	public RenderInputDelegate(ViewGroup parentView) {
		customRenderers = new HashMap<ListType, RenderInputFieldInterface>();
		renderField = new RenderInputFieldHelper(parentView);
	}
	
	
	/**
	 * Registers a custom renderer for the given FormElement type
	 * This overriddes the default rendering of the FormElement type
	 * @param type, the FormElement for which the custom renderer is registered.
	 * @param renderer, the renderer that is called when the fields are rendered.
	 */
	public void registerCustomFieldRenderer(ListType type, RenderInputFieldInterface renderer) {
		
		if (type == null) {
			throw new InvalidParameterException("Error registering CustomRenderer, type may not be null");
		}
		if (renderer == null) {
			throw new InvalidParameterException("Error registering CustomRenderer, renderer may not be null");
		}
		customRenderers.put(type, renderer);
	
	}
	
	
	/**
	 * Registers a custom tooltip renderer which is used for showing tooltips
	 * @param renderer, the renderer that is called when the tooltips are rendered.
	 */
	public void registerCustomTooltipRenderer(RenderTooltipInterface renderer) {
		renderField.registerCustomTooltipRenderer(renderer);
	}


	/**
	 * Renders all PaymentProductField fields that are given in the list fields
	 * This is a delegate who defers the rendering to the specific RenderInputField implementation
	 *
	 * @param paymentItem, the paymentproduct that contains all fields that are going to be rendered
	 * @param accountOnFile, the selected accountOnFile that contains previous payment data
	 * @param inputDataPersister, the paymentRequest which contains all entered user input
	 */
	public void renderPaymentInputFields(PaymentItem paymentItem, AccountOnFile accountOnFile, InputDataPersister inputDataPersister, PaymentContext paymentContext) {

		if (paymentItem == null) {
			throw new InvalidParameterException("Error rendering PaymentInputFields, basicPaymentItem may not be null");
		}

		// Render all fields
		RenderInputRegistry registry = new RenderInputRegistry(customRenderers);

		for (PaymentProductField field : paymentItem.getPaymentProductFields()) {

			RenderInputFieldInterface renderer = registry.getRenderInputFieldForFieldType(field.getDisplayHints().getFormElement().getType());
			if (renderer != null) {
				renderField.renderField(renderer, field, paymentItem, accountOnFile, inputDataPersister, paymentContext);
			}
		}
	}


	/**
	 * Hides all tooltiptexts
	 */
	public void hideTooltipTexts(ViewGroup parentView) {
		
		// Loop trough all the children to find a tooltip view
		for (int childIndex = 0; childIndex < parentView.getChildCount(); childIndex ++) {
			
			View child = parentView.getChildAt(childIndex);
			if (child.getTag() != null && child.getTag() instanceof String) {
				
				String tag = (String)child.getTag();
				if (tag.startsWith(RenderTooltipInterface.TOOLTIP_TAG)) {
					
					// Remove the view
					ViewGroup removeViewParent = ((ViewGroup)child.getParent());
					removeViewParent.removeView(child);
				}
			}
		}
	}
		
}