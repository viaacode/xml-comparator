package be.viaa.tools.xmldiff;

import org.xmlunit.builder.Input;

import javax.xml.transform.Source;
import java.io.File;

/**
 * Created by dieter on 26/06/2018.
 */
public class SourceGenerator {
    public static Source fromFile(File file) {
        return Input.fromFile(file).build();
    }

    public static Source fromString(String contents) {
        return Input.fromString(contents).build();
    }
}
