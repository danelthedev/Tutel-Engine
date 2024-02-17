package org.tuiasi.engine.ui.components.basicComponents.tree;

import org.tuiasi.engine.ui.components.IComponent;

import java.util.List;

public interface ITree extends IComponent {

    void setTreeData(List<TreeNode> treeData);
    void setNodeClickListener(TreeListener listener);

}
