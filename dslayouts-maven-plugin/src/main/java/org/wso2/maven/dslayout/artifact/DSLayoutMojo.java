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

package org.wso2.maven.dslayout.artifact;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.wso2.maven.dslayout.artifact.utils.FileManagementUtils;

import java.io.File;

@Mojo(name = "buildDSLayout") public class DSLayoutMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.basedir}") private File path;

    @Parameter(defaultValue = "zip") private String type;

    @Parameter(defaultValue = "${project}") private MavenProject mavenProject;

    private static String LAYOUT_CONTENT_DIR = "layout";
    private static String SEPERATOR = "-";
    private static String FILE_TYPE_SEPERATOR = ".";

    public void execute() throws MojoExecutionException, MojoFailureException {
        File project = path;
        File layoutContentDir = new File(project, LAYOUT_CONTENT_DIR);
        createZip(layoutContentDir);
    }

    /**
     * Create archive file using artifact contect and set it to maven repo
     *
     * @param project content directory of theme artifact
     * @throws MojoExecutionException
     */
    private void createZip(File project) throws MojoExecutionException {
        try {
            String artifactType = getType();
            String artifactName =
                    mavenProject.getArtifactId() + SEPERATOR + mavenProject.getVersion() + FILE_TYPE_SEPERATOR
                            + artifactType;
            File archive = FileManagementUtils.createArchive(path, project, artifactName);
            if (archive != null && archive.exists()) {
                mavenProject.getArtifact().setFile(archive);
            } else {
                throw new MojoExecutionException(archive + " is null or doesn't exist");
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error while creating layout archive", e);
        }

    }

    private String getType() {
        return type;
    }
}
