package net.renalias.xmlvalidator.ui.components;

import net.renalias.xmlvalidator.core.ValidationError;

import java.util.EventListener;

public interface ElementDoubleClickedListener extends EventListener {
	public void onElementDoubleClicked(ValidationError element);
}
