/*
 * Copyright (C) 2016 BBC
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

package com.bbc.remarc.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.bbc.remarc.util.Configuration;

/**
 * Servlet context listener to create a directory on the web server containing
 * sample resources (images, audio, video) - will be replaced when admin/file
 * upload is complete
 * 
 * @author Peter Brock
 */
public class ResourceServletContextListener implements
		ServletContextListener {

	private final static Logger log = Logger
			.getLogger(ResourceServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.debug("contextInitialized");

		String resourcesPath = System.getenv(Configuration.ENV_DATA_DIR_OPENSHIFT);

		if (resourcesPath == null || "".equals(resourcesPath)) {
			log.info("Not running on OpenShift. Falling back to local resource path");

			resourcesPath = System.getenv(Configuration.ENV_DATA_DIR_LOCAL);
		}
		
		createResourceFolders(resourcesPath);

		event.getServletContext().setAttribute(Configuration.ATT_DATA_DIR, resourcesPath);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("contextDestroyed");

	}

	private void createResourceFolders(String resourcesPath) {

		log.debug("checking resource internal folders...");
		
		File audioDir = new File(resourcesPath + Configuration.CONTENT_DIR + Configuration.AUDIO_DIR_NAME);
		File imagesDir = new File(resourcesPath + Configuration.CONTENT_DIR + Configuration.IMAGE_DIR_NAME);
		File videoDir = new File(resourcesPath + Configuration.CONTENT_DIR + Configuration.VIDEO_DIR_NAME);
		File uploadDir = new File(resourcesPath + Configuration.UPLOAD_DIR_NAME);

		try {

			// create the audio, images, video and upload folders
			if (!audioDir.exists()) {
				FileUtils.forceMkdir(audioDir);
				log.debug("audio folder created");
			};

			if (!imagesDir.exists()) {
				FileUtils.forceMkdir(imagesDir);
				log.debug("images folder created");
			};

			if (!videoDir.exists()) {
				FileUtils.forceMkdir(videoDir);
				log.debug("video folder created");
			};

			if (!uploadDir.exists()) {
				FileUtils.forceMkdir(uploadDir);
				log.debug("upload folder created");
			};

		} catch (IOException e) {
			log.error("Error creating resources content folders, may lead to unexpected behaviour: "
					+ e);
		}
	}

}