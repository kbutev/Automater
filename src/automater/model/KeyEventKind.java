/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

/**
 *
 * @author Kristiyan Butev
 */
public enum KeyEventKind {
    press, release, tap;

    public boolean isReleaseOrTap() {
        return this == release || this == tap;
    }
    
    @Override
    public String toString() {
        switch (this) {
            case press: return "press";
            case release: return "release";
            case tap: return "tap";
            default: return "";
        }
    }
}
