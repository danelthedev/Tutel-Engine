package org.tuiasi.engine.ui.components.basicComponents.searchbar;

import org.tuiasi.engine.ui.components.IListener;

public interface SearchListener extends IListener {
    void onSearch(String searchText);
}