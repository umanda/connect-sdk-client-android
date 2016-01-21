package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

/**
 * Defines the rendering of label interface methods
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderLabelInterface {
	
	
	/**
	 * Renders a tooltip by the data in the PaymentProductField.
	 * This label is added to the given rowView
	 * 
	 * @param selectedPaymentProduct, the selected PaymentProduct, used for getting the correct translations
	 * @param rowView, the ViewGroup to which the rendered label is added
	 */
	public void renderLabel(PaymentProductField field, PaymentProduct selectedPaymentProduct, ViewGroup rowView);
	
}