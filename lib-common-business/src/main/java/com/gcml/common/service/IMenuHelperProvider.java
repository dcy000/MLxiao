package com.gcml.common.service;

import com.gcml.common.menu.EMenu;
import com.gcml.common.menu.MenuHelperProviderImp;

public interface IMenuHelperProvider {
    void menu(EMenu menu, MenuHelperProviderImp.MenuResult callback);
}
