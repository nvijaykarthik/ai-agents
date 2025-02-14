package in.nvijaykarthik.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtil {
    
    public static String readFileToString(String filePath) {
        try {
            Resource resource= new ClassPathResource(filePath);
            resource.exists();
            File file=resource.getFile();
            return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
