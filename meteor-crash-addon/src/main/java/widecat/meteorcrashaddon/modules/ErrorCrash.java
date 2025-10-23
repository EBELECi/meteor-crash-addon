package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import widecat.meteorcrashaddon.CrashAddon;

public class ErrorCrash extends Module {
    public ErrorCrash() {
        super(CrashAddon.CATEGORY, "error-crash", "Temporarily disabled for 1.21.8 compatibility.");
    }
}