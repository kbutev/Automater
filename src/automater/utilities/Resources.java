/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;

/**
 * Collection of resource utility methods and some resource constant values.
 *
 * @author Byti
 */
public class Resources {

    public final static String RESOURCES_DIRECTORY_PATH = "resources";

    public final static String DEFAULT_IMAGE_EXTENSION = "png";
    public final static String PNG_EXTENSION = "png";
    public final static String JPG_EXTENSION = "jpg";
    public final static String JPEG_EXTENSION = "jpeg";

    public static @NotNull Path getImagePath(@NotNull String key) {
        return Path.getLocalDirectory()
                .withSubpath(RESOURCES_DIRECTORY_PATH)
                .withSubpath(key)
                .withFileExtension(DEFAULT_IMAGE_EXTENSION);
    }
}
