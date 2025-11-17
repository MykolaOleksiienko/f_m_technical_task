package enums.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewportPreset {
    DESKTOP_1920x1080(1920, 1080),
    DESKTOP_1366x768(1366, 768),
    TABLET_768x1024(768, 1024),
    MOBILE_375x667(375, 667),
    MOBILE_360x640(360, 640);

    private final int width;
    private final int height;

    public static ViewportPreset fromDimensions(int width, int height) {
        for (ViewportPreset preset : values()) {
            if (preset.width == width && preset.height == height) {
                return preset;
            }
        }
        // Return default if not found in enum, but allow custom dimensions
        return DESKTOP_1920x1080;
    }
}
