package keystrokesmod;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Unmodifiable
public final class Const {
    public static final String NAME = "SilenceFix";
    public static final String VERSION = "3.0.2";
    public static final List<String> CHANGELOG = Collections.unmodifiableList(Arrays.asList(
            "[*] update version to 3.0.2",
            "[*] fix Unexpected StringIndexOutOfBoundsException while 'isTeamMate' in'Utils' String index out of range:2",
            "[!] This is SilenceFix XD"
    ));
}
