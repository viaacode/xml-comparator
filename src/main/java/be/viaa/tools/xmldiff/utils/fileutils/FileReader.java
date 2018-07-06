package be.viaa.tools.xmldiff.utils.fileutils;

import java.io.File;

/**
 * Created by VIAA on 26/06/2018.
 */
public class FileReader {
    public File readFile(String path) {
        return new File(getClass().getClassLoader().getResource(path).getFile());
    }
}
