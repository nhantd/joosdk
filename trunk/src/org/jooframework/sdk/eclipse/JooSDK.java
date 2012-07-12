package org.jooframework.sdk.eclipse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
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
	
	public static void addNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		
		// Add the nature
		if (!project.hasNature(JOONature.NATURE_ID)) {
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = JOONature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
	}
	
	public static void createDefaultStructure(IContainer container, IProgressMonitor monitor) throws CoreException, IOException {
		/* Add the resource folder */
		final IFolder resourceFolder = container.getFolder(new Path("static"));
		resourceFolder.create(true, true, monitor);
		
		final IFolder appFolder = container.getFolder(new Path("static/app"));
		appFolder.create(true, true, monitor);
		
		final IFolder frameworkFolder = container.getFolder(new Path("static/framework"));
		frameworkFolder.create(true, true, monitor);
		
		/* Add the stylesheet folder */
		final IFolder styleFolder = container.getFolder(new Path("static/app/css"));
		styleFolder.create(true, true, monitor);
//		final IFolder defaultStyleFolder = container.getFolder(new Path("static/app/css/default"));
//		defaultStyleFolder.create(true, true, monitor);
		final IFolder frameworkStyleFolder = container.getFolder(new Path("static/framework/css"));
		frameworkStyleFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/framework/css/joo-2.0.3.css"),
				getResource("/templates/joo-2.0.3.css"), monitor);
		
		/* Add the images folder */
		final IFolder imagesFolder = container.getFolder(new Path("static/images"));
		imagesFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/images/logo.jpg"),
				getResource("/templates/logo.jpg"), monitor);
		addFileToProject(container, new Path("static/images/image-default.png"),
				getResource("/templates/image-default.png"), monitor);
		
		/* Add the favicon */
//		addFileToProject(container, new Path("static/images/favicon.ico"),
//				getResource("/templates/favicon.ico"), monitor);
		
		/* Add the js folder */
		final IFolder jsFolder = container.getFolder(new Path("static/app/js"));
		jsFolder.create(true, true, monitor);
		
		final IFolder jsAppPortletFolder = container.getFolder(new Path("static/app/js/portlets"));
		jsAppPortletFolder.create(true, true, monitor);
		
		final IFolder jsAppPluginFolder = container.getFolder(new Path("static/app/js/plugins"));
		jsAppPluginFolder.create(true, true, monitor);
		
		final IFolder jsAppViewFolder = container.getFolder(new Path("static/app/js/views"));
		jsAppViewFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/app/js/main.js"),
				getResource("/templates/main.js"), monitor);
		
		/* Add the js framework folder */
		final IFolder jsFwFolder = container.getFolder(new Path("static/framework/js"));
		jsFwFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/framework/js/joo-2.0.3r14.js"),
				getResource("/templates/joo-2.0.3r14.js"), monitor);
		addFileToProject(container, new Path("static/framework/js/class-name-injection.js"),
				getResource("/templates/class-name-injection.js"), monitor);
		
		/* Add the js 3rd party folder */
		final IFolder jsThirdPartyFolder = container.getFolder(new Path("static/thirdparty"));
		jsThirdPartyFolder.create(true, true, monitor);
		
		/* Add the microtemplating folder */
		final IFolder templateFolder = container.getFolder(new Path("static/app/microtemplating"));
		templateFolder.create(true, true, monitor);
//		final IFolder defaultTemplateFolder = container.getFolder(new Path("static/app/microtemplating/default"));
//		defaultTemplateFolder.create(true, true, monitor);

		/* Add the layout file */
		addFileToProject(container, new Path("static/app/microtemplating/layout.htm"),
				getResource("/templates/layout.htm"), monitor);
		
		/* Add the template file */
		addFileToProject(container, new Path("static/app/microtemplating/template.htm"),
				getResource("/templates/template.htm"), monitor);
		
		/* Add the plugin file */
		addFileToProject(container, new Path("static/app/microtemplating/plugin.htm"),
				getResource("/templates/plugin.htm"), monitor);
		
		/* Add the development main page file */
		addFileToProject(container, new Path("index.copy.html"),
				getResource("/templates/index.copy.html"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("index.html"),
				getResource("/templates/index.html"), monitor);
		
		/* Sample files */
		final IFolder defaultSampleFolder = container.getFolder(new Path("static/app/js/portlets/samples"));
		defaultSampleFolder.create(true, true, monitor);
		final IFolder defaultSampleTemplateFolder = container.getFolder(new Path("static/app/microtemplating/samples"));
		defaultSampleTemplateFolder.create(true, true, monitor);
		
		addFileToProject(container, new Path("static/app/css/sample.css"),
				getResource("/templates/samples/sample.css"), monitor);
		addFileToProject(container, new Path("static/app/js/portlets/samples/SamplePortlet.js"),
				getResource("/templates/samples/SamplePortlet.js"), monitor);
		addFileToProject(container, new Path("static/app/microtemplating/samples/SamplePortlet.htm"),
				getResource("/templates/samples/SamplePortlet.htm"), monitor);

		final IFolder distFolder = container.getFolder(new Path("dist"));
		distFolder.create(true, true, monitor);
//		final IFolder distDefaultFolder = container.getFolder(new Path("dist"));
//		distDefaultFolder.create(true, true, monitor);
		/* Pre-built files */
		addFileToProject(container, new Path("dist/all.css"),
				getResource("/templates/build/all.css"), monitor);
		addFileToProject(container, new Path("dist/all.js"),
				getResource("/templates/build/all.js"), monitor);
		addFileToProject(container, new Path("dist/all.txt"),
				getResource("/templates/build/all.txt"), monitor);
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
		Pattern p = Pattern.compile("index\\.(.*)(\\.){0,1}copy\\.html");
		Matcher m = p.matcher(name);
		String version = null;
		if (m.find()) {
			version = m.group(1);
		} else {
			return;
		}
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(getFileContent(file)));
			Document doc = builder.parse(is);
			
			IContainer parent = file.getParent();
			IFolder distFolder = parent.getFolder(new Path("dist"));
			if (!distFolder.exists()) {
				distFolder.create(true, true, null);
			}
			if (!version.isEmpty()) {
				if (version.lastIndexOf('.') == version.length() - 1) {
					version = version.substring(0, version.length()-1);
				}
				IFolder distVersionedFolder = parent.getFolder(new Path("dist/"+version));
				if (!distVersionedFolder.exists())
					distVersionedFolder.create(true, true, null);
				version += "/";
			}

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
			writeFile(file.getParent(), "dist/"+version+"all.js", sb.toString());

			//build all.css
			sb = new StringBuilder();
			nl = doc.getElementsByTagName("link");
			for(int i=0;i<nl.getLength();i++) {
				String src = extractAttribute(nl.item(i), "href");
				if(src != null && src.length() > 0) {
					sb.append(getFileContent(file.getParent(), src));
				}
			}
			writeFile(file.getParent(), "dist/"+version+"all.css", sb.toString());
			
			//build all.txt
			sb = new StringBuilder();
			ArrayList<String> htmlArray = new ArrayList<String>();
			htmlArray.add("static/app/microtemplating/"+version+"template.htm");
			htmlArray.add("static/app/microtemplating/"+version+"layout.htm");
			htmlArray.add("static/app/microtemplating/"+version+"plugin.htm");
			nl = doc.getElementsByTagName("link");
			for(String src: srcArray) {
				if (src.indexOf("static/app/js/portlets/") != -1) {
					src = src.replaceFirst("static/app/js/portlets/", "static/app/microtemplating/"+version);
					src = src.replaceFirst(".js", ".htm");
					htmlArray.add(src);
				} else if (src.indexOf("static/app/js/views/") != -1) {
					src = src.replaceFirst("static/app/js/views/", "static/app/microtemplating/views/"+version);
					src = src.replaceFirst(".js", ".htm");
					htmlArray.add(src);
				}
			}
			for(String src: htmlArray) {
				sb.append(getFileContent(file.getParent(), src));
			}
			writeFile(file.getParent(), "dist/"+version+"all.txt", sb.toString());
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
