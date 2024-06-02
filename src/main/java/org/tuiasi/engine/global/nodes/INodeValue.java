package org.tuiasi.engine.global.nodes;

public interface INodeValue {

    Object saveState();
    void loadState(Object state);

//    String serialize();
//    void deserialize(String serialized);
}
