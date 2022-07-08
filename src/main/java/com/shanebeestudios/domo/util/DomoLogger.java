package com.shanebeestudios.domo.util;

import java.util.logging.Logger;

public class DomoLogger extends Logger {

    protected DomoLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /** Get an instance of LoggerBee
     * @return new instance of LoggerBee
     */
    public static DomoLogger getLogger() {
        return new DomoLogger("", null);
    }

    @Override
    public void info(String msg) {
        String prefix = msg.replace("[NBTAPI]", "&7[&bNBT&3API&7]");
        Util.log(prefix);
    }

}
