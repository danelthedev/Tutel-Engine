package org.tuiasi.engine.ui.composedComponents;

import imgui.ImGui;
import org.tuiasi.engine.ui.basicComponents.button.ButtonWithTitle;
import org.tuiasi.engine.ui.basicComponents.button.IButton;
import org.tuiasi.engine.ui.basicComponents.checkbox.CheckboxWithTitle;
import org.tuiasi.engine.ui.basicComponents.checkbox.ICheckbox;
import org.tuiasi.engine.ui.basicComponents.dropdown.DropdownWithTitle;
import org.tuiasi.engine.ui.basicComponents.dropdown.IDropdown;
import org.tuiasi.engine.ui.basicComponents.list.IList;
import org.tuiasi.engine.ui.basicComponents.list.ListWithTitle;
import org.tuiasi.engine.ui.basicComponents.searchbar.ISearchbar;
import org.tuiasi.engine.ui.basicComponents.searchbar.SearchbarWithHint;
import org.tuiasi.engine.ui.basicComponents.textbox.ITextbox;
import org.tuiasi.engine.ui.basicComponents.textbox.Textbox;
import org.tuiasi.engine.ui.basicComponents.tree.ITree;
import org.tuiasi.engine.ui.basicComponents.tree.TreeNode;
import org.tuiasi.engine.ui.basicComponents.tree.TreeWithTitle;
import org.tuiasi.engine.ui.basicComponents.tree.TreeWithTitleAndSearchBar;

import java.util.List;

public class DemoWindow implements IImguiWindow{

    String title;
    ICheckbox checkbox;
    IDropdown dropdown;
    IButton button;
    TreeWithTitleAndSearchBar treeWithSearchbar;
    IList list;
    ITextbox textbox;

    public DemoWindow(String title) {
        this.title = title;
        checkbox = new CheckboxWithTitle("Demo checkbox", false, isChecked -> System.out.println("Checkbox 1 state: " + isChecked));
        dropdown = new DropdownWithTitle("Demo dropdown", new String[]{"Option 0", "Option 1", "Option 2"}, 0, index -> System.out.println("Dropdown selected index: " + index));
        button = new ButtonWithTitle("Demo button", () -> {
            System.out.println("Button clicked");
            // Set the text in the textbox to "test" when the button is clicked
            textbox.setText("test");
        });
        TreeNode root = new TreeNode("Root");
        TreeNode child1 = new TreeNode("Child 1");
        TreeNode child2 = new TreeNode("Child 2");
        TreeNode child3 = new TreeNode("Child 3");

        root.setChildren(List.of(child1, child2, child3));
        child1.setChildren(List.of(new TreeNode("Child 1.1"), new TreeNode("Child 1.2")));
        child2.setChildren(List.of(new TreeNode("Child 2.1"), new TreeNode("Child 2.2")));
        child3.setChildren(List.of(new TreeNode("Child 3.1"), new TreeNode("Child 3.2")));

        TreeNode grandChild1 = new TreeNode("Grandchild 1");
        TreeNode grandChild2 = new TreeNode("Nepotu 2");
        child1.setChildren(List.of(grandChild1, grandChild2));
        grandChild1.setChildren(List.of(new TreeNode("Grandchild 1.1"), new TreeNode("Grandchild 1.2")));
        grandChild2.setChildren(List.of(new TreeNode("Grandchild 2.1"), new TreeNode("Grandchild 2.2")));
        treeWithSearchbar = new TreeWithTitleAndSearchBar("Search in tree", "Demo tree", List.of(root), node -> System.out.println("Node clicked: " + node.getName()), searchText -> System.out.println("Search text: " + searchText));

        list = new ListWithTitle("Demo list", List.of("Item 1", "Item 2", "Item 3"), 0, item -> System.out.println("Item clicked: " + item));

        textbox = new Textbox("", text -> System.out.println("Textbox text: " + text));
    }

    public void render() {
        ImGui.separator();
        checkbox.render();
        ImGui.separator();
        dropdown.render();
        ImGui.separator();
        button.render();
        ImGui.separator();
        treeWithSearchbar.render();
        ImGui.separator();
        list.render();
        ImGui.separator();
        textbox.render();
        ImGui.separator();
    }
}
