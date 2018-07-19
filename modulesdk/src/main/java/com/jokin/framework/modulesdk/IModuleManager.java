package com.jokin.framework.modulesdk;

/**
 * Created by jokin on 2018/7/16 10:52.
 */

public interface IModuleManager {
    void destroy();

    void registerModule(IClientModule module);

    void unregisterModule(IClientModule module);
}
