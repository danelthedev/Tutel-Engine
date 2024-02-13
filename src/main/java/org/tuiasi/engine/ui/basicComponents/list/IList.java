package org.tuiasi.engine.ui.basicComponents.list;

public interface IList {
        void render();

        interface ItemClickListener {
                void onItemClick(String node);
        }
}
