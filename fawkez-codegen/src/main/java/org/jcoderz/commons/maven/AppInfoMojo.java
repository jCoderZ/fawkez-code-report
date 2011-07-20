package org.jcoderz.commons.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jcoderz.commons.util.XsltBase;

/**
 * The simple type task generates Java classes from an XML file.
 * 
 * @goal appinfo
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * 
 * @author mrumpf
 * 
 */
public class AppInfoMojo extends AbstractMojo {
	/**
	 * <i>Maven Internal</i>: Project to interact with.
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The XSL stylesheet file.
	 * 
	 * @parameter default-value= "generate-log-message-info.xsl"
	 */
	private String xslFile = null;

	/**
	 * An include pattern for the simple type definition files.
	 * 
	 * @parameter default-value="app.info.xml"
	 */
	private String includePattern;

	// TODO:
	// http://code.hammerpig.com/search-for-files-in-directory-using-wildcards-in-java.html
	public void execute() throws MojoExecutionException {
		processFolder("main");
		processFolder("test");
	}

	private void processFolder (String category)
	{
		File sourceDirectory = new File(project.getBasedir(), "src" + File.separator + category + File.separator + "fawkez");
		File destDirectory = new File(project.getBuild().getDirectory(), "generated-fawkez-" + category);
		if (!destDirectory.exists())
		{
			destDirectory.mkdirs();
		}
		List<File> files = findFiles(sourceDirectory, includePattern);
		for (File file : files) {
			String log = file.getName() + "-" + category + ".log";
			getLog().info("" + file.getName() + " -> " + destDirectory.getName());
			XsltBase.transform(file, xslFile, destDirectory, new File(project
					.getBuild().getDirectory(), log), false);
		}
	}

	private List<File> findFiles(File dir, String pattern) {
		List<File> files = new ArrayList<File>();
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith((pattern))) {
				files.add(file);
			}
		}
		return files;
	}
}