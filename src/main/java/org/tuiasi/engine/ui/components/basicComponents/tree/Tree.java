package org.tuiasi.engine.ui.components.basicComponents.tree;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import lombok.*;

import java.util.List;

@Setter @Getter @RequiredArgsConstructor @NoArgsConstructor
public class Tree extends ITree{
    private String label;
    @NonNull
    private List<TreeNode> treeData;
    private TreeListener nodeClickListener;

    private Integer lastClickedNodeId;  // Variable to store the last clicked node

    public void addNode(TreeNode node) {
        treeData.add(node);
    }

    public Tree(List<TreeNode> treeData, TreeListener nodeClickListener) {
        this.treeData = treeData;
        this.nodeClickListener = nodeClickListener;
    }

    @Override
    public void render() {
        // Draw a separator as the top border
        renderTreeNodes(treeData);
    }

    private void renderTreeNodes(List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            // Push ID to ensure a unique ID for each node
            ImGui.pushID(node.hashCode());

            // Get node data
            boolean isLeaf = node.getChildren().isEmpty();
            int flags = isLeaf ? ImGuiTreeNodeFlags.Leaf : (ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick);
            flags |= ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.SpanAvailWidth;

            // Check if the current node is the last clicked one
            boolean isLastClickedNode = lastClickedNodeId != null && lastClickedNodeId.equals(node.hashCode());

            // Highlight the last clicked node
            if (isLastClickedNode) {
                ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 1.0f, 0.0f, 1.0f);  // Yellow text color
            }

            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 5);
            // Render the tree node
            boolean treeNodeOpen = ImGui.treeNodeEx(node.getName() + "##TreeNode", flags);

            // Check if the item (label) is clicked, regardless of whether the node is open or closed
            if (ImGui.isItemClicked()) {
                lastClickedNodeId = node.hashCode();  // Update the last clicked node ID
                if (nodeClickListener != null) {
                    nodeClickListener.onNodeClick(node);
                }
            }
            ImGui.popStyleVar();

            // Restore the text color if not the last clicked node
            if (isLastClickedNode) {
                ImGui.popStyleColor();
            }

            if (treeNodeOpen) {
                // Recursively render child nodes
                renderTreeNodes(node.getChildren());
                // Close the tree node if it's not a leaf
                ImGui.treePop();
            }

            // Pop the ID to balance the stack
            ImGui.popID();
        }
    }



    @Override
    public void setTreeData(List<TreeNode> treeData) {
        this.treeData = treeData;
    }

    @Override
    public void setNodeClickListener(TreeListener listener) {
        this.nodeClickListener = listener;
    }

}
