package org.jooframework.sdk.eclipse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

	private static String url = "http://internal.joolist.com/";

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
	
	public static void createGameStructure(IContainer container,
			IProgressMonitor monitor) throws CoreException, IOException {
		final IFolder srcFolder = container.getFolder(new Path(
				"src"));
		srcFolder.create(true, true, monitor);
		
		final IFolder srcViewsFolder = container.getFolder(new Path(
				"src/views"));
		srcViewsFolder.create(true, true, monitor);
		
		final IFolder srcMainFolder = container.getFolder(new Path(
				"src/main"));
		srcMainFolder.create(true, true, monitor);
		
		final IFolder jsAppViewStateFolder = container.getFolder(new Path(
				"src/views/state"));
		jsAppViewStateFolder.create(true, true, monitor);
		
		final IFolder jsAppViewStatePlayFolder = container.getFolder(new Path(
				"src/views/state/play"));
		jsAppViewStatePlayFolder.create(true, true, monitor);

		addFileToProject(container, new Path("src/main/main.js"),
				getResource("/templates/main_game.js"), monitor);

		/* Add the layout file */
		addFileToProject(container, new Path(
				"src/main/layout.htm"),
				getResource("/templates/layout_game.htm"), monitor);

		/* Add the development main page file */
		addFileToProject(container, new Path("index.copy.html"),
				getResource("/templates/index.copy_game.html"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("index.html"),
				getResource("/templates/index_game.html"), monitor);

		/* Sample files */
		addFileToProject(container, new Path(
				"src/views/state/play/PlayState.js"),
				getResource("/templates/samples/PlayState.js"), monitor);
		addFileToProject(container, new Path(
				"src/views/state/play/PlayState.htm"),
				getResource("/templates/samples/PlayState.htm"), monitor);
	}

	public static void createDefaultStructure(IContainer container,
			IProgressMonitor monitor) throws CoreException, IOException {
		/* Add the resource folder */
		final IFolder resourceFolder = container.getFolder(new Path("static"));
		resourceFolder.create(true, true, monitor);

		final IFolder appFolder = container.getFolder(new Path("static/app"));
		appFolder.create(true, true, monitor);

		final IFolder frameworkFolder = container.getFolder(new Path(
				"static/framework"));
		frameworkFolder.create(true, true, monitor);

		/* Add the stylesheet folder */
		final IFolder styleFolder = container.getFolder(new Path(
				"static/app/css"));
		styleFolder.create(true, true, monitor);
		final IFolder frameworkStyleFolder = container.getFolder(new Path(
				"static/framework/css"));
		frameworkStyleFolder.create(true, true, monitor);

		addFileToProject(container, new Path(
				"static/framework/css/joo-2.0.3.css"),
				getResource("/templates/joo-2.0.3.css"), monitor);

		/* Add the js folder */
		final IFolder jsFolder = container.getFolder(new Path("static/app/js"));
		jsFolder.create(true, true, monitor);

		final IFolder jsAppPortletFolder = container.getFolder(new Path(
				"static/app/js/portlets"));
		jsAppPortletFolder.create(true, true, monitor);

		final IFolder jsAppPluginFolder = container.getFolder(new Path(
				"static/app/js/plugins"));
		jsAppPluginFolder.create(true, true, monitor);

		final IFolder jsAppViewFolder = container.getFolder(new Path(
				"static/app/js/views"));
		jsAppViewFolder.create(true, true, monitor);

		addFileToProject(container, new Path("static/app/js/main.js"),
				getResource("/templates/main.js"), monitor);

		/* Add the js framework folder */
		final IFolder jsFwFolder = container.getFolder(new Path(
				"static/framework/js"));
		jsFwFolder.create(true, true, monitor);

		addFileToProject(container, new Path(
				"static/framework/js/joo-2.0.3r41.js"),
				getResource("/templates/joo-2.0.3r41.js"), monitor);
		addFileToProject(container, new Path(
				"static/framework/js/class-name-injection.js"),
				getResource("/templates/class-name-injection.js"), monitor);

		/* Add the js 3rd party folder */
		final IFolder jsThirdPartyFolder = container.getFolder(new Path(
				"static/thirdparty"));
		jsThirdPartyFolder.create(true, true, monitor);

		/* Add the microtemplating folder */
		final IFolder templateFolder = container.getFolder(new Path(
				"static/app/microtemplating"));
		templateFolder.create(true, true, monitor);

		/* Add the layout file */
		addFileToProject(container, new Path(
				"static/app/microtemplating/layout.htm"),
				getResource("/templates/layout.htm"), monitor);

		/* Add the template file */
		addFileToProject(container, new Path(
				"static/app/microtemplating/template.htm"),
				getResource("/templates/template.htm"), monitor);

		/* Add the development main page file */
		addFileToProject(container, new Path("index.copy.html"),
				getResource("/templates/index.copy.html"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("index.html"),
				getResource("/templates/index.html"), monitor);

		/* Sample files */
		final IFolder defaultSampleFolder = container.getFolder(new Path(
				"static/app/js/portlets/samples"));
		defaultSampleFolder.create(true, true, monitor);
		final IFolder defaultSampleTemplateFolder = container
				.getFolder(new Path("static/app/microtemplating/samples"));
		defaultSampleTemplateFolder.create(true, true, monitor);

		addFileToProject(container, new Path("static/app/css/sample.css"),
				getResource("/templates/samples/sample.css"), monitor);
		addFileToProject(container, new Path(
				"static/app/js/portlets/samples/SamplePortlet.js"),
				getResource("/templates/samples/SamplePortlet.js"), monitor);
		addFileToProject(container, new Path(
				"static/app/microtemplating/samples/SamplePortlet.htm"),
				getResource("/templates/samples/SamplePortlet.htm"), monitor);

		final IFolder distFolder = container.getFolder(new Path("dist"));
		distFolder.create(true, true, monitor);
		// final IFolder distDefaultFolder = container.getFolder(new
		// Path("dist"));
		// distDefaultFolder.create(true, true, monitor);
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
					IStatus.OK, "static unavailable: "
							+ path.toFile().getPath(), null);
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

	public static void update(IFile file) {
		String name = file.getName();
		if (name.startsWith("index.")) {
			updateFile(file);
		}
	}
	
	public static void update(IContainer f) {
		try {
			IResource[] members = f.members();
			for (IResource member : members) {
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					String fname = file.getName();
					if (fname.startsWith("index.")
							&& fname.endsWith(".copy.html")) {
						updateFile(file);
					} else if (fname.equalsIgnoreCase("joo-build.xml")) {
						readAndUpdate(file);
					}
				}
			}
		} catch (CoreException e) {

		}
	}
	
	public static void updateCat(IContainer f) {
		try {
			IResource[] members = f.members();
			for (IResource member : members) {
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					String fname = file.getName();
					if (fname.startsWith("index.")
							&& fname.endsWith(".copy.html")) {
						updateCatFile(file);
					} else if (fname.equalsIgnoreCase("joo-build.xml")) {
						readAndUpdateCat(file);
					}
				}
			}
		} catch (CoreException e) {

		}
	}
	
	public static void updateCat(IFile file) {
		String fname = file.getName();
		if (fname.startsWith("index.")
				&& fname.endsWith(".copy.html")) {
			updateCatFile(file);
		}
	}
	
	private static void updateCatFile(IFile file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(
					getFileContent(file)));
			Document doc = builder.parse(is);

			IContainer parent = file.getParent();
			IFolder compsFolder = parent.getFolder(new Path("cats"));
			if (!compsFolder.exists())
				compsFolder.create(true, true, null);

			// logging in Cerberus
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(
					url
							+ "demo/cerberus/service/index/login?email=catexport@terrabook.vn&password=catexport@2013");
			CloseableHttpResponse res = client.execute(get);
			res.close();

			NodeList nl = doc.getElementsByTagName("script");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String bundle = extractAttribute(n, "bundle-id");
				String type = extractAttribute(n, "type");
				String base = extractAttribute(n, "base");
				String resizeStr = extractAttribute(n, "resize");
				int resize = 100;
				if (resizeStr != null && !resizeStr.isEmpty()) {
					try {
						resize = Integer.parseInt(resizeStr);
					} catch (Exception ex) {
						
					}
				}
					
				if (bundle == null) continue;
				
				if (base == null || base.isEmpty()) base = "cats";
				
				if (type.equals("cat/comp")) {
					// create folder for corresponding bundle
					IFolder bundleFolder = null;
					String path = base + "/" + bundle;
					bundleFolder = parent.getFolder(new Path(path));
					createFolders(bundleFolder);
	
					// download and extract bundle from Repo
					HttpGet httpGet = new HttpGet(url
							+ "demo/cat-export/object/download?bundle=" + bundle + "&resize="+resize);
					res = client.execute(httpGet);
					try {
						HttpEntity entity = res.getEntity();
						InputStream ist = entity.getContent();
						
						IFolder mediaFolder = bundleFolder.getFolder("media");
						if (mediaFolder.exists()) {
							IResource[] members = mediaFolder.members();
							for (IResource member : members) {
								if (member instanceof IFile) {
									((IFile) member).delete(true, false, null);
								}
							}
						}
							
						IFile zipFile = writeFile(bundleFolder, "bundle.zip", ist);
						EntityUtils.consume(entity);
	
						unZip(zipFile, bundleFolder);
						
						zipFile.delete(true, null);
					} finally {
						res.close();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void build(IFile file) {
		String name = file.getName();
		if (name.startsWith("index.")) {
			buildFile(file);
		}
	}

	public static void build(IContainer f) {
		try {
			IResource[] members = f.members();
			for (IResource member : members) {
				if (member instanceof IFile) {
					IFile file = (IFile) member;
					String fname = file.getName();
					if (fname.startsWith("index.")
							&& fname.endsWith(".copy.html")) {
						buildFile(file);
					} else if (fname.equalsIgnoreCase("joo-build.xml")) {
						readAndBuild(file);
					}
				}
			}
		} catch (CoreException e) {

		}
	}
	
	private static void readAndUpdate(IFile file, UpdateJobInterface handler) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(
					getFileContent(file)));
			Document doc = builder.parse(is);

			IContainer parent = file.getParent();
			NodeList nl = doc.getElementsByTagName("build-script");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String href = extractAttribute(n, "href");
				System.out.println(href);
				IFile updateFile = parent.getFile(new Path(href));
				handler.onUpdate(updateFile);
			}
		} catch (Exception ex) {

		}
	}
	
	private static void readAndBuild(IFile file) {
		readAndUpdate(file, new UpdateJobInterface() {
			
			@Override
			public void onUpdate(IFile file) {
				buildFile(file);
			}
		});
	}
	
	private static void readAndUpdate(IFile file) {
		readAndUpdate(file, new UpdateJobInterface() {
			
			@Override
			public void onUpdate(IFile file) {
				updateFile(file);
			}
		});
	}
	
	private static void readAndUpdateCat(IFile file) {
		readAndUpdate(file, new UpdateJobInterface() {
			
			@Override
			public void onUpdate(IFile file) {
				updateCatFile(file);
			}
		});
	}

	private static void updateFile(IFile file) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(
					getFileContent(file)));
			Document doc = builder.parse(is);

			IContainer parent = file.getParent();
			IFolder compsFolder = parent.getFolder(new Path("components"));
			if (!compsFolder.exists())
				compsFolder.create(true, true, null);

			// logging in Cerberus
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(
					url
							+ "demo/cerberus/service/index/login?email=coderepo@joolist.com&password=coderepo@2013");
			CloseableHttpResponse res = client.execute(get);
			res.close();

			NodeList nl = doc.getElementsByTagName("script");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String type = extractAttribute(n, "type");
				String bundle = extractAttribute(n, "bundle-id");
				String locked = extractAttribute(n, "lock");
				String target = extractAttribute(n, "target");
				String base = extractAttribute(n, "base");
				
				if (locked != null && locked.equals("true")) continue;
				
				if (type != null && type.equals("joo/comp") && bundle != null) {
					// create folder for corresponding bundle
					IFolder bundleFolder = null;
					String path = "";
					if (target != null && !target.isEmpty()) {
						path = target;
					} else {
						if (base == null || base.isEmpty()) {
							base = "components";
						}
						path = base + "/" + bundle;
					}
					bundleFolder = parent.getFolder(new Path(path));
					createFolders(bundleFolder);
	
					// download and extract bundle from Repo
					HttpGet httpGet = new HttpGet(url
							+ "demo/files/file/download-bundle?bundle=" + bundle);
					res = client.execute(httpGet);
					try {
						HttpEntity entity = res.getEntity();
						InputStream ist = entity.getContent();
	
						IFile zipFile = writeFile(bundleFolder, "bundle.zip", ist);
						EntityUtils.consume(entity);
	
						unZip(zipFile, bundleFolder);
						
						zipFile.delete(true, null);
					} finally {
						res.close();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
			InputSource is = new InputSource(new StringReader(
					getFileContent(file)));
			Document doc = builder.parse(is);

			IContainer parent = file.getParent();
			IFolder distFolder = parent.getFolder(new Path("dist"));
			if (!distFolder.exists()) {
				distFolder.create(true, true, null);
			}
			String distPath = "dist/";
			if (!version.isEmpty()) {
				if (version.lastIndexOf('.') == version.length() - 1) {
					version = version.substring(0, version.length() - 1);
				}
				distPath = "dist-"+ version;
				IFolder distVersionedFolder = parent.getFolder(new Path(distPath));
				if (!distVersionedFolder.exists())
					distVersionedFolder.create(true, true, null);
				distPath += "/";
				version += "/";
			}
			
			ArrayList<String> srcArray = new ArrayList<String>();
			ArrayList<String> htmlArray = new ArrayList<String>();
			StringBuilder cssSb = new StringBuilder();
			StringBuilder sb1 = new StringBuilder();
			
			NodeList nl = doc.getElementsByTagName("link");
			for (int i = 0; i < nl.getLength(); i++) {
				String src = extractAttribute(nl.item(i), "href");
				if (src != null && src.length() > 0) {
					cssSb.append(getFileContent(file.getParent(), src));
				}
			}
			
			// build all.js
			StringBuilder sb = new StringBuilder();
			nl = doc.getElementsByTagName("script");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				
				String type = extractAttribute(n, "type");
				String bundle = extractAttribute(n, "bundle-id");
				String target = extractAttribute(n, "target");
				String base = extractAttribute(n, "base");
				
				if (type != null && type.equals("joo/comp")) {
					String path = "";
					if (target != null && !target.isEmpty()) {
						path = target;
					} else {
						if (base == null || base.isEmpty()) {
							base = "components";
						}
						path = base + "/" + bundle;
					}
					IFolder f = parent.getFolder(new Path(path));
					if (f.exists()) {
						//loop through the files here
						putComponentFiles(f, cssSb, sb1, sb);
					}
				} else {
					String src = extractAttribute(n, "src");
					String build = extractAttribute(n, "build");
					if (src != null && src.length() > 0
							&& (build == null || !build.equals("no"))) {
						sb.append(getFileContent(file.getParent(), src));
						srcArray.add(src);
					}
				}
			}
			writeFile(file.getParent(), distPath + "all.js",
					sb.toString());

			// build all.css
			writeFile(file.getParent(), distPath + "all.css",
					cssSb.toString());

			// build all.txt
			htmlArray.add("static/app/microtemplating/" + version
					+ "template.htm");
			htmlArray.add("static/app/microtemplating/" + version
					+ "layout.htm");
			htmlArray.add("static/app/microtemplating/" + version
					+ "plugin.htm");
			nl = doc.getElementsByTagName("link");
			for (String src : srcArray) {
				if (src.indexOf("static/app/js/portlets/") != -1) {
					src = src.replaceFirst("static/app/js/portlets/",
							"static/app/microtemplating/" + version);
					src = src.replaceFirst(".js", ".htm");
					htmlArray.add(src);
				} else if (src.indexOf("static/app/js/views/") != -1) {
					src = src.replaceFirst("static/app/js/views/",
							"static/app/microtemplating/views/" + version);
					src = src.replaceFirst(".js", ".htm");
					htmlArray.add(src);
				}
			}
			for (String src : htmlArray) {
				sb1.append(getFileContent(file.getParent(), src));
			}
			writeFile(file.getParent(), distPath + "all.txt",
					sb1.toString());
		} catch (Exception ex) {

		}
	}

	private static void putComponentFiles(IFolder f, StringBuilder cssSb,
			StringBuilder htmlSb, StringBuilder sb) throws CoreException {
		IResource[] members = f.members();
		for (IResource member : members) {
			if (member instanceof IFile) {
				IFile fileMember = (IFile) member;
				String fname = fileMember.getName();
				if (fname.endsWith(".css")) {
					cssSb.append(getFileContent(fileMember));
				} else if (fname.endsWith(".htm")) {
					htmlSb.append(getFileContent(fileMember));
				} else if (fname.endsWith(".js")) {
					System.out.println("append "+fname);
					sb.append(getFileContent(fileMember));
				}
			} else if (member instanceof IFolder) {
				putComponentFiles((IFolder)member, cssSb, htmlSb, sb);
			}
		}
	}
	
	private static IFile writeFile(IContainer parent, String file, String content)
			throws UnsupportedEncodingException, CoreException {
		return writeFile(parent, file,
				new ByteArrayInputStream(content.getBytes("utf-8")));
	}

	private static IFile writeFile(IContainer parent, String file, InputStream is)
			throws UnsupportedEncodingException, CoreException {
		IFile f = parent.getFile(new Path(file));
		if (f.exists()) {
			f.setContents(is, true, true, null);
		} else {
			f.create(is, true, null);
		}
		return f;
	}

	private static String getFileContent(IFile file) {
		if (file != null && file.exists() && file.isAccessible()) {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(
						file.getContents(), "utf-8"));
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
					} catch (Exception ex) {
					}
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
	
	private static void createFolders(IContainer container) throws CoreException {
		ArrayList<IFolder> folders = new ArrayList<IFolder>();
		while (container != null && container instanceof IFolder && !container.exists()) {
			folders.add((IFolder)container);
			container = container.getParent();
		}
		
		for(int i=folders.size()-1; i>=0; i--) {
			IFolder folder = folders.get(i);
			folder.create(true, true, null);
		}
	}

	private static void unZip(IFile zipFile, IFolder outputFolder) {
		try {

			// create output directory is not exists
			if (!outputFolder.exists()) {
				outputFolder.create(true, true, null);
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(zipFile.getContents());
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			
			while (ze != null) {
				String fileName = ze.getName();
				IFile f = outputFolder.getFile(fileName);
				IContainer container = f.getParent();

				createFolders(container);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(zis, baos);
				byte b[] = baos.toByteArray();
				baos.flush();
				baos.close();
				writeFile(outputFolder, fileName, new ByteArrayInputStream(b));
				
				ze = zis.getNextEntry();
			}
			
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
	}

}

interface UpdateJobInterface {
	
	public void onUpdate(IFile file);
}