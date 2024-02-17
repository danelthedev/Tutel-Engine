package org.tuiasi.engine.logic;

import org.tuiasi.engine.ui.AppWindow;

public interface IAppLogic {

    void init() throws Exception;

    void input(AppWindow appWindow);

    void update(float delta);

    void render(AppWindow appWindow);
}
