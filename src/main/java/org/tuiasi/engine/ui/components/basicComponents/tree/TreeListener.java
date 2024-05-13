package org.tuiasi.engine.ui.components.basicComponents.tree;

import org.tuiasi.engine.global.nodes.Node;

public interface TreeListener {
    void onNodeClick(Node<?> node);
}