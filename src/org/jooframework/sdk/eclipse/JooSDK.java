package org.jooframework.sdk.eclipse;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class JooSDK {
	
	public static void createDefaultStructure(IContainer container, IProgressMonitor monitor) throws CoreException, IOException {
		/* Add the resource folder */
		final IFolder resourceFolder = container.getFolder(new Path("resource"));
		resourceFolder.create(true, true, monitor);
		
		/* Add the stylesheet folder */
		final IFolder styleFolder = container.getFolder(new Path("resource/css"));
		styleFolder.create(true, true, monitor);
		final IFolder defaultStyleFolder = container.getFolder(new Path("resource/css/default"));
		defaultStyleFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("resource/css/default/portlet.css"),
				getResource("/templates/portlet.css"), monitor);
		
		/* Add the images folder */
		final IFolder imagesFolder = container.getFolder(new Path("resource/images"));
		imagesFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("resource/images/logo.jpg"),
				getResource("/templates/logo.jpg"), monitor);
		
		/* Add the favicon */
//		addFileToProject(container, new Path("resource/images/favicon.ico"),
//				getResource("/templates/favicon.ico"), monitor);
		
		/* Add the js folder */
		final IFolder jsFolder = container.getFolder(new Path("resource/js"));
		jsFolder.create(true, true, monitor);
		
		/* Add the js app folder */
		final IFolder jsAppFolder = container.getFolder(new Path("resource/js/app"));
		jsAppFolder.create(true, true, monitor);
		
		final IFolder jsAppPortletFolder = container.getFolder(new Path("resource/js/app/portlets"));
		jsAppPortletFolder.create(true, true, monitor);
		
		final IFolder jsAppPluginFolder = container.getFolder(new Path("resource/js/app/plugins"));
		jsAppPluginFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("resource/js/app/index.default.js"),
				getResource("/templates/index.default.js"), monitor);
		
		/* Add the js framework folder */
		final IFolder jsFwFolder = container.getFolder(new Path("resource/js/framework"));
		jsFwFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("resource/js/framework/base.js"),
				getResource("/templates/base.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/bootstrap.js"),
				getResource("/templates/bootstrap.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/event.js"),
				getResource("/templates/event.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/memcached.js"),
				getResource("/templates/memcached.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/observer.js"),
				getResource("/templates/observer.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/page.js"),
				getResource("/templates/page.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/plugins.js"),
				getResource("/templates/plugins.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/portlet.js"),
				getResource("/templates/portlet.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/request.js"),
				getResource("/templates/request.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/tools.js"),
				getResource("/templates/tools.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/ui.js"),
				getResource("/templates/ui.js"), monitor);
		addFileToProject(container, new Path("resource/js/framework/utils.js"),
				getResource("/templates/utils.js"), monitor);
		
		/* Add the js lib folder */
		final IFolder jsLibFolder = container.getFolder(new Path("resource/js/lib"));
		jsLibFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("resource/js/lib/inherit.js"),
				getResource("/templates/inherit.js"), monitor);
		addFileToProject(container, new Path("resource/js/lib/jquery-1.7.1.min.js"),
				getResource("/templates/jquery-1.7.1.min.js"), monitor);
		addFileToProject(container, new Path("resource/js/lib/jquery-ui.min.js"),
				getResource("/templates/jquery-ui.min.js"), monitor);
		addFileToProject(container, new Path("resource/js/lib/jquery.cookie.js"),
				getResource("/templates/jquery.cookie.js"), monitor);
		addFileToProject(container, new Path("resource/js/lib/jquery.json-2.2.js"),
				getResource("/templates/jquery.json-2.2.js"), monitor);
		addFileToProject(container, new Path("resource/js/lib/micro-templating.js"),
				getResource("/templates/micro-templating.js"), monitor);
		
		/* Add the js 3rd party folder */
		final IFolder jsThirdPartyFolder = container.getFolder(new Path("resource/js/thirdparty"));
		jsThirdPartyFolder.create(true, true, monitor);
		
		/* Add the microtemplating folder */
		final IFolder templateFolder = container.getFolder(new Path("resource/microtemplating"));
		templateFolder.create(true, true, monitor);
		final IFolder defaultTemplateFolder = container.getFolder(new Path("resource/microtemplating/default"));
		defaultTemplateFolder.create(true, true, monitor);

		/* Add the layout file */
		addFileToProject(container, new Path("resource/microtemplating/default/layout.htm"),
				getResource("/templates/layout.htm"), monitor);
		
		/* Add the template file */
		addFileToProject(container, new Path("resource/microtemplating/default/template.htm"),
				getResource("/templates/template.htm"), monitor);
		
		/* Add the plugin file */
		addFileToProject(container, new Path("resource/microtemplating/default/plugin.htm"),
				getResource("/templates/plugin.htm"), monitor);
		
		/* Add the development main page file */
		addFileToProject(container, new Path("index.default.copy.html"),
				getResource("/templates/index.default.copy.html"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("index.html"),
				getResource("/templates/index.html"), monitor);
		
		addFileToProject(container, new Path("build.php"),
				getResource("/templates/tools/build.php"), monitor);
		addFileToProject(container, new Path("simple_html_dom.php"),
				getResource("/templates/tools/simple_html_dom.php"), monitor);
		
		/* Sample files */
		final IFolder defaultSampleFolder = container.getFolder(new Path("resource/js/app/portlets/samples"));
		defaultSampleFolder.create(true, true, monitor);
		final IFolder defaultSampleTemplateFolder = container.getFolder(new Path("resource/microtemplating/default/samples"));
		defaultSampleTemplateFolder.create(true, true, monitor);
		addFileToProject(container, new Path("resource/css/default/sample.css"),
				getResource("/templates/samples/sample.css"), monitor);
		addFileToProject(container, new Path("resource/js/app/portlets/samples/SamplePortlet.js"),
				getResource("/templates/samples/SamplePortlet.js"), monitor);
		addFileToProject(container, new Path("resource/microtemplating/default/samples/SamplePortlet.htm"),
				getResource("/templates/samples/SamplePortlet.htm"), monitor);

		/* Pre-built files */
		addFileToProject(container, new Path("resource/css/all.default.css"),
				getResource("/templates/build/all.default.css"), monitor);
		addFileToProject(container, new Path("resource/js/all.default.js"),
				getResource("/templates/build/all.default.js"), monitor);
		addFileToProject(container, new Path("resource/microtemplating/all.default.txt"),
				getResource("/templates/build/all.default.txt"), monitor);
	}
	
	private static InputStream getResource(String string) {
		return JooSDK.class.getResourceAsStream(string);
	}
	
	/**
     * Adds a new file to the project.
     * 
     * @param container
     * @param path
     * @param contentStream
     * @param monitor
     * @throws CoreException
	 * @throws IOException 
     */
    private static void addFileToProject(IContainer container, Path path,
            InputStream contentStream, IProgressMonitor monitor)
            throws CoreException, IOException {
        if (contentStream == null) {
        	IStatus status = new Status(IStatus.ERROR, "JooProjectWizard",
					IStatus.OK, "Resource unavailable: "+path.toFile().getPath(), null);
			throw new CoreException(status);
        }
    	
    	final IFile file = container.getFile(path);
        
        if (file.exists()) {
            file.setContents(contentStream, true, true, monitor);
        } else {
            file.create(contentStream, true, monitor);
        }
        
        contentStream.close();
    }
}
