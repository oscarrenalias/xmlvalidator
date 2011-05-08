package net.renalias.xmlvalidator.ui.components;

import net.renalias.xmlvalidator.core.ValidationError;

import java.util.EventListener;

public interface ElementSelectedListener extends EventListener {
	public void onElementSelected(ValidationError element);
}
