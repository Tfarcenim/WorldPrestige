package tfar.worldprestige.platform;

import java.util.List;

public interface MLConfig {
    List<String> getRequiredAdvancements();
    boolean useDefaultPrestige();
    boolean deleteWorldOnFailure();
}
