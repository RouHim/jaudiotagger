package org.jaudiotagger.audio.generic;

//import java.io.IOException;  ==Android==
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.attribute.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Outputs permissions to try and identify why we dont have permissions to read/write file
 */
public class Permissions {
    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.generic");

    // ==Android==
//    /**
//     * Display Permissions
//     *
//     * @param path
//     * @return
//     */
//    public static String displayPermissions(Path path)
//    {
//        if (TagOptionSingleton.getInstance().isAndroid())
//        {
//            return "Android Unknown";
//        }
//        else
//        {
//            StringBuilder sb = new StringBuilder();
//            sb.append("File " + path + " permissions\n");
//            try {
//                {
//                    AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
//                    if (view != null) {
//                        sb.append("owner:" + view.getOwner().getName() + "\n");
//                        for (AclEntry acl : view.getAcl()) {
//                            sb.append(acl + "\n");
//                        }
//                    }
//                }
//
//                {
//                    PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class);
//                    if (view != null) {
//                        PosixFileAttributes pfa = view.readAttributes();
//                        sb.append(":owner:" +
//                                          pfa.owner().getName() +
//                                          ":group:" +
//                                          pfa.group().getName() +
//                                          ":" +
//                                          PosixFilePermissions.toString(pfa.permissions()) +
//                                          "\n");
//                    }
//                }
//            } catch (IOException ioe) {
//                logger.error("Unable to read permissions for:" + path.toString());
//            }
//            return sb.toString();
//        }
//    }
}
