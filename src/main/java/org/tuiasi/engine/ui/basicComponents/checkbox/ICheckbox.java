package org.tuiasi.engine.ui.basicComponents.checkbox;

public interface ICheckbox {

    void render();

    interface CheckboxListener {
        void onToggle(boolean isChecked);
    }
}
