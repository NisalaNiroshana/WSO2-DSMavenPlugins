/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.maven.carbondashboards.dstheme.artifact.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManagementUtils {

    private static final Logger logger = Logger.getLogger(FileManagementUtils.class);

    private static final String ERROR_CREATING_CORRESPONDING_ZIP_FILE = "Error creating corresponding ZIP file";
    private static final String TARGET = "target";
    private static final String DSGADGET_TEMP = "dsgtheme-tmp";
    private static final String PROJECT_EXTENTION = ".project";

    /**
     * create a archive file using given location,artifact name and artifact location
     *
     * @param location         path of artifact directory
     * @param artifactLocation artifact location
     * @param artifactName     name of the artifact
     * @return File
     * @throws Exception
     */
    public static File createArchive(File location, File artifactLocation, String artifactName) throws Exception {
        File targetFolder;
        targetFolder = new File(location.getPath(), TARGET);
        File themeDataFolder = new File(targetFolder, DSGADGET_TEMP);
        if (!themeDataFolder.mkdirs()) {
            logger.error(ERROR_CREATING_CORRESPONDING_ZIP_FILE);
        }
        File zipFolder = new File(themeDataFolder, artifactLocation.getName());
        if (!zipFolder.mkdirs()) {
            logger.error(ERROR_CREATING_CORRESPONDING_ZIP_FILE);
        }
        ;
        FileUtils.copyDirectory(artifactLocation, zipFolder);
        File zipFile = new File(targetFolder, artifactName);
        zipFolder(zipFolder.getAbsolutePath(), zipFile.toString());
        FileUtils.deleteDirectory(themeDataFolder);
        return zipFile;
    }

    /**
     * Zipping given source folder into destination zip file
     *
     * @param srcFolder   source folder
     * @param destZipFile destination zip file
     */
    private static void zipFolder(String srcFolder, String destZipFile) {
        try (FileOutputStream fileWriter = new FileOutputStream(destZipFile);
                ZipOutputStream zip = new ZipOutputStream(fileWriter)) {
            addFolderContentsToZip(srcFolder, zip);
            zip.flush();
        } catch (IOException ex) {
            logger.error(ERROR_CREATING_CORRESPONDING_ZIP_FILE, ex);
        }
    }

    /**
     * adding source file into zip file
     *
     * @param path    Path
     * @param srcFile source file
     * @param zip     destination zip file
     */
    private static void addToZip(String path, String srcFile, ZipOutputStream zip) {

        File folder = new File(srcFile);

        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            if (!srcFile.equals(PROJECT_EXTENTION)) {
                byte[] buf = new byte[1024];
                int len;
                try (FileInputStream in = new FileInputStream(srcFile)) {
                    String location = folder.getName();
                    if (!path.equalsIgnoreCase("")) {
                        location = path + File.separator + folder.getName();
                    }
                    zip.putNextEntry(new ZipEntry(location));
                    while ((len = in.read(buf)) > 0) {
                        zip.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    logger.error(ERROR_CREATING_CORRESPONDING_ZIP_FILE, e);
                }
            }
        }
    }

    /**
     * adding folder contents to the zip file.
     *
     * @param srcFolder directory of content files
     * @param zip       destination zip file
     */
    private static void addFolderContentsToZip(String srcFolder, ZipOutputStream zip) {
        File folder = new File(srcFolder);
        String fileListArray[] = folder.list();
        int i = 0;
        if (fileListArray != null) {
            while (i < fileListArray.length) {
                addToZip("", srcFolder + File.separator + fileListArray[i], zip);
                i++;
            }
        }
    }

    /**
     * adding given folder to the Zip file
     *
     * @param path      Path
     * @param srcFolder source folder
     * @param zip       destination zip file
     */
    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) {
        File folder = new File(srcFolder);
        String fileListArray[] = folder.list();
        int i = 0;
        if (fileListArray != null) {
            while (i < fileListArray.length) {
                String newPath = folder.getName();
                if (!path.equalsIgnoreCase("")) {
                    newPath = path + File.separator + newPath;
                }
                addToZip(newPath, srcFolder + File.separator + fileListArray[i], zip);
                i++;
            }
        }
    }
}