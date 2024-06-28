package org.tuiasi.engine.ui.components.basicComponents.checkbox;

import org.tuiasi.engine.ui.components.IListener;

public interface CheckboxListener extends IListener {
    void onToggle(boolean isChecked);
}