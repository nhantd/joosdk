package org.jooframework.sdk.eclipse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JooSDK {
	
	public static void createDefaultStructure(IContainer container, IProgressMonitor monitor) throws CoreException, IOException {
		/* Add the resource folder */
		final IFolder resourceFolder = container.getFolder(new Path("static"));
		resourceFolder.create(true, true, monitor);
		
		/* Add the stylesheet folder */
		final IFolder styleFolder = container.getFolder(new Path("static/css"));
		styleFolder.create(true, true, monitor);
		final IFolder defaultStyleFolder = container.getFolder(new Path("static/css/default"));
		defaultStyleFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/css/default/portlet.css"),
				getResource("/templates/portlet.css"), monitor);
		
		/* Add the images folder */
		final IFolder imagesFolder = container.getFolder(new Path("static/images"));
		imagesFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/images/logo.jpg"),
				getResource("/templates/logo.jpg"), monitor);
		
		/* Add the favicon */
//		addFileToProject(container, new Path("static/images/favicon.ico"),
//				getResource("/templates/favicon.ico"), monitor);
		
		/* Add the js folder */
		final IFolder jsFolder = container.getFolder(new Path("static/js"));
		jsFolder.create(true, true, monitor);
		
		/* Add the js app folder */
		final IFolder jsAppFolder = container.getFolder(new Path("static/js/app"));
		jsAppFolder.create(true, true, monitor);
		
		final IFolder jsAppPortletFolder = container.getFolder(new Path("static/js/app/portlets"));
		jsAppPortletFolder.create(true, true, monitor);
		
		final IFolder jsAppPluginFolder = container.getFolder(new Path("static/js/app/plugins"));
		jsAppPluginFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/js/app/index.default.js"),
				getResource("/templates/index.default.js"), monitor);
		
		/* Add the js framework folder */
		final IFolder jsFwFolder = container.getFolder(new Path("static/js/framework"));
		jsFwFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/js/framework/base.js"),
				getResource("/templates/base.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/bootstrap.js"),
				getResource("/templates/bootstrap.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/event.js"),
				getResource("/templates/event.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/memcached.js"),
				getResource("/templates/memcached.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/observer.js"),
				getResource("/templates/observer.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/page.js"),
				getResource("/templates/page.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/plugins.js"),
				getResource("/templates/plugins.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/portlet.js"),
				getResource("/templates/portlet.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/request.js"),
				getResource("/templates/request.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/tools.js"),
				getResource("/templates/tools.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/ui.js"),
				getResource("/templates/ui.js"), monitor);
		addFileToProject(container, new Path("static/js/framework/utils.js"),
				getResource("/templates/utils.js"), monitor);
		
		/* Add the js lib folder */
		final IFolder jsLibFolder = container.getFolder(new Path("static/js/lib"));
		jsLibFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/js/lib/inherit.js"),
				getResource("/templates/inherit.js"), monitor);
		addFileToProject(container, new Path("static/js/lib/jquery-1.7.1.min.js"),
				getResource("/templates/jquery-1.7.1.min.js"), monitor);
		addFileToProject(container, new Path("static/js/lib/jquery-ui.min.js"),
				getResource("/templates/jquery-ui.min.js"), monitor);
		addFileToProject(container, new Path("static/js/lib/jquery.cookie.js"),
				getResource("/templates/jquery.cookie.js"), monitor);
		addFileToProject(container, new Path("static/js/lib/jquery.json-2.2.js"),
				getResource("/templates/jquery.json-2.2.js"), monitor);
		addFileToProject(container, new Path("static/js/lib/micro-templating.js"),
				getResource("/templates/micro-templating.js"), monitor);
		
		/* Add the js 3rd party folder */
		final IFolder jsThirdPartyFolder = container.getFolder(new Path("static/js/thirdparty"));
		jsThirdPartyFolder.create(true, true, monitor);
		
		/* Add the microtemplating folder */
		final IFolder templateFolder = container.getFolder(new Path("static/microtemplating"));
		templateFolder.create(true, true, monitor);
		final IFolder defaultTemplateFolder = container.getFolder(new Path("static/microtemplating/default"));
		defaultTemplateFolder.create(true, true, monitor);

		/* Add the layout file */
		addFileToProject(container, new Path("static/microtemplating/default/layout.htm"),
				getResource("/templates/layout.htm"), monitor);
		
		/* Add the template file */
		addFileToProject(container, new Path("static/microtemplating/default/template.htm"),
				getResource("/templates/template.htm"), monitor);
		
		/* Add the plugin file */
		addFileToProject(container, new Path("static/microtemplating/default/plugin.htm"),
				getResource("/templates/plugin.htm"), monitor);
		
		/* Add the development main page file */
		addFileToProject(container, new Path("index.default.copy.html"),
				getResource("/templates/index.default.copy.html"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("index.html"),
				getResource("/templates/index.html"), monitor);
		
		/* Sample files */
		final IFolder defaultSampleFolder = container.getFolder(new Path("static/js/app/portlets/samples"));
		defaultSampleFolder.create(true, true, monitor);
		final IFolder defaultSampleTemplateFolder = container.getFolder(new Path("static/microtemplating/default/samples"));
		defaultSampleTemplateFolder.create(true, true, monitor);
		addFileToProject(container, new Path("static/css/default/sample.css"),
				getResource("/templates/samples/sample.css"), monitor);
		addFileToProject(container, new Path("static/js/app/portlets/samples/SamplePortlet.js"),
				getResource("/templates/samples/SamplePortlet.js"), monitor);
		addFileToProject(container, new Path("static/microtemplating/default/samples/SamplePortlet.htm"),
				getResource("/templates/samples/SamplePortlet.htm"), monitor);

		/* Pre-built files */
		addFileToProject(container, new Path("static/css/all.default.css"),
				getResource("/templates/build/all.default.css"), monitor);
		addFileToProject(container, new Path("static/js/all.default.js"),
				getResource("/templates/build/all.default.js"), monitor);
		addFileToProject(container, new Path("static/microtemplating/all.default.txt"),
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
					IStatus.OK, "static unavailable: "+path.toFile().getPath(), null);
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

	public static void build(IFile file) {
		String name = file.getName();
		if (name.startsWith("index.")) {
			buildFile(file);
		}
	}

	public static void build(IContainer f) {
		try {
			IResource []members = f.members();
			for(IResource member: members) {
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					String fname = file.getName();
					if (fname.startsWith("index.") && fname.endsWith(".copy.html")) {
						buildFile(file);
					}
				}
			}
		} catch (CoreException e) {
			
		}
	}
	
	private static void buildFile(IFile file) {
		String name = file.getName();
		String version = name.substring(6, name.length()-10);
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(getFileContent(file)));
			Document doc = builder.parse(is);

			//build all.js
			StringBuilder sb = new StringBuilder();
			NodeList nl = doc.getElementsByTagName("script");
			ArrayList<String> srcArray = new ArrayList<String>();
			for(int i=0;i<nl.getLength();i++) {
				Node n = nl.item(i);
				String src = extractAttribute(n, "src");
				String build = extractAttribute(n, "build");
				if(src != null && src.length() > 0 && (build == null || !build.equals("no"))) {
					sb.append(getFileContent(file.getParent(), src));
					srcArray.add(src);
				}
			}
			writeFile(file.getParent(), "static/js/all."+version+".js", sb.toString());

			//build all.css
			sb = new StringBuilder();
			nl = doc.getElementsByTagName("link");
			for(int i=0;i<nl.getLength();i++) {
				String src = extractAttribute(nl.item(i), "href");
				if(src != null && src.length() > 0) {
					sb.append(getFileContent(file.getParent(), src));
				}
			}
			writeFile(file.getParent(), "static/css/all."+version+".css", sb.toString());
			
			//build all.txt
			sb = new StringBuilder();
			ArrayList<String> htmlArray = new ArrayList<String>();
			htmlArray.add("static/microtemplating/"+version+"/template.htm");
			htmlArray.add("static/microtemplating/"+version+"/layout.htm");
			htmlArray.add("static/microtemplating/"+version+"/plugin.htm");
			nl = doc.getElementsByTagName("link");
			for(String src: srcArray) {
				if (src.indexOf("static/js/app/portlets/") != -1) {
					src = src.replaceFirst("static/js/app/portlets/", "static/microtemplating/"+version+"/");
					src = src.replaceFirst(".js", ".htm");
					htmlArray.add(src);
				}
			}
			for(String src: htmlArray) {
				sb.append(getFileContent(file.getParent(), src));
			}
			writeFile(file.getParent(), "static/microtemplating/all."+version+".txt", sb.toString());
		} catch (Exception ex) {
		}
	}

	private static void writeFile(IContainer parent, String file, String content) throws UnsupportedEncodingException, CoreException {
		IFile f = parent.getFile(new Path(file));
		if (f.exists()) {
            f.setContents(new ByteArrayInputStream(content.getBytes("utf-8")), true, true, null);
        } else {
            f.create(new ByteArrayInputStream(content.getBytes("utf-8")), true, null);
        }
	}
	
	private static String getFileContent(IFile file) {
		if (file != null && file.exists() && file.isAccessible()) {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(file.getContents(), "utf-8"));
				String s = null;
				while ((s = br.readLine()) != null) {
					sb.append(s);
					sb.append("\n");
				}
				return sb.toString();
			} catch (Exception ex) {
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (Exception ex) {}
				}
			}
		}
		return "";
	}

	private static String getFileContent(IContainer parent, String src) {
		IFile file = parent.getFile(new Path(src));
		return getFileContent(file);
	}

	private static String extractAttribute(Node node, String attr) {
		if (node instanceof Element) {
			Element e = (Element) node;
			return e.getAttribute(attr);
		}
		return null;
	}
}
