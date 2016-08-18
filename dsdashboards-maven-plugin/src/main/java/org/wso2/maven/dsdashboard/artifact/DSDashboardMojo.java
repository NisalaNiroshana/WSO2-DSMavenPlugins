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

package org.wso2.maven.dsdashboard.artifact;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

@Mojo(name = "buildDSDashboard") public class DSDashboardMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.basedir}") private File path;

    @Parameter(defaultValue = "${project}") private MavenProject mavenProject;

    private static String DASHBOARD_CONTENT_DIR = "dashboard";

    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        File project = path;
        File dashboardContentDir = new File(project, DASHBOARD_CONTENT_DIR);
        setDashboardJSONToRepo(dashboardContentDir);
    }

    /**
     * Set dashboard json into maven repository
     *
     * @param dashboardContentDir dashboard json file
     * @throws MojoExecutionException
     */
    private void setDashboardJSONToRepo(File dashboardContentDir) throws MojoExecutionException {
        if (dashboardContentDir.isDirectory()) {
            File[] dashboardList = dashboardContentDir.listFiles();
            for (int dashboardCount = 0; dashboardCount < dashboardList.length; dashboardCount++) {
                if (dashboardList[dashboardCount] != null && dashboardList[dashboardCount].exists()) {
                    mavenProject.getArtifact().setFile(dashboardList[dashboardCount]);
                } else {
                    throw new MojoExecutionException(dashboardList[dashboardCount] + " is null or doesn't exist");
                }
            }
        }
    }
}
