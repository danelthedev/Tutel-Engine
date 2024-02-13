package org.tuiasi.engine.ui.basicComponents.searchbar;

public interface ISearchbar {

    void render();

    interface SearchListener {
        void onSearch(String searchText);
    }

}
