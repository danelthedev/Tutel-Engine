package org.tuiasi.engine.ui.basicComponents.tree;

import java.util.List;

public interface ITree {

    void render();
    void setTreeData(List<TreeNode> treeData);
    void setNodeClickListener(TreeListener listener);

}
