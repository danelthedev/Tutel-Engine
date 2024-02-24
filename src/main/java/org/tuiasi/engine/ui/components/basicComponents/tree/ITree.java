package org.tuiasi.engine.ui.components.basicComponents.tree;

import org.tuiasi.engine.ui.components.IComponent;

import java.util.List;

public abstract class ITree extends IComponent {

    public abstract void setTreeData(List<TreeNode> treeData);
    public abstract void setNodeClickListener(TreeListener listener);

}
