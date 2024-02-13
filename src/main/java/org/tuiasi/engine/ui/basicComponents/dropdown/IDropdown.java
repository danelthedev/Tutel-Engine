package org.tuiasi.engine.ui.basicComponents.dropdown;

public interface IDropdown {
    void render();

    interface IDropdownListener {
        void onItemSelected(int index);
    }

}
