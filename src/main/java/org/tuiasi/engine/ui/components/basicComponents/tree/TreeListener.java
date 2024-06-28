package org.tuiasi.engine.ui.components.basicComponents.tree;

import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.ui.components.IListener;

public interface TreeListener extends IListener {
    void onNodeClick(Node<?> node);
}