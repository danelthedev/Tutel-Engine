package org.tuiasi.engine.ui.components.basicComponents.tree;

import org.tuiasi.engine.global.nodes.Node;
import org.tuiasi.engine.ui.components.IComponent;

public abstract class ITree extends IComponent {

    public abstract void setRoot(Node<?> root);
    public abstract void setNodeClickListener(TreeListener listener);

    public abstract Node<?> getNodeByName(String name);
    public abstract Node<?> getNodeByPath(String path);

}
