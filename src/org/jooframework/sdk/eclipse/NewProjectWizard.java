package org.jooframework.sdk.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class NewProjectWizard extends Wizard implements IExecutableExtension,
		INewWizard {

	private WizardNewProjectCreationPage wizardPage;

	private IConfigurationElement config;

	private IWorkbench workbench;

	private IProject project;

	@Override
	public boolean performFinish() {
		if (project != null) {
			return true;
		}

		final IProject projectHandle = wizardPage.getProjectHandle();

		URI projectURI = (!wizardPage.useDefaults()) ? wizardPage
				.getLocationURI() : null;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		final IProjectDescription desc = workspace
				.newProjectDescription(projectHandle.getName());

		desc.setLocationURI(projectURI);

		/*
		 * Just like the ExampleWizard, but this time with an operation object
		 * that modifies workspaces.
		 */
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(desc, projectHandle, monitor);
			}
		};

		/*
		 * This isn't as robust as the code in the BasicNewProjectResourceWizard
		 * class. Consider beefing this up to improve error handling.
		 */
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}

		project = projectHandle;

		if (project == null) {
			return false;
		}

		BasicNewProjectResourceWizard.updatePerspective(config);
		BasicNewProjectResourceWizard.selectAndReveal(project, workbench
				.getActiveWorkbenchWindow());

		return true;
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	public void addPages() {
		/*
		 * Unlike the custom new wizard, we just add the pre-defined one and
		 * don't necessarily define our own.
		 */
		wizardPage = new WizardNewProjectCreationPage("NewJooProjectWizard");
		wizardPage.setDescription("Create a new Joo Project.");
		wizardPage.setTitle("New Joo Project");
		addPage(wizardPage);
	}

	/**
	 * This creates the project in the workspace.
	 * 
	 * @param description
	 * @param projectHandle
	 * @param monitor
	 * @throws CoreException
	 * @throws OperationCanceledException
	 */
	void createProject(IProjectDescription description, IProject proj,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {
			monitor.beginTask("", 2000);

			proj.create(description, new SubProgressMonitor(monitor, 1000));

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(
					monitor, 1000));

			/*
			 * Okay, now we have the project and we can do more things with it
			 * before updating the perspective.
			 */
			IContainer container = (IContainer) proj;
			
			addFiles(container, monitor);
		} catch (IOException ioe) {
			IStatus status = new Status(IStatus.ERROR, "ExampleWizard",
					IStatus.OK, ioe.getLocalizedMessage(), null);
			throw new CoreException(status);
		} finally {
			monitor.done();
		}
	}
	
    private void addFiles(IContainer container, IProgressMonitor monitor) throws CoreException, IOException {
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
		addFileToProject(container, new Path("resource/microtemplating/default/layout.htm"),
				getResource("/templates/template.htm"), monitor);
		
		/* Add the plugin file */
		addFileToProject(container, new Path("resource/microtemplating/default/plugin.htm"),
				getResource("/templates/plugin.htm"), monitor);
		
		/* Add the development main page file */
		addFileToProject(container, new Path("resource/index.default.htm"),
				getResource("/templates/index.default.htm"), monitor);

		/* Add the production main page file */
		addFileToProject(container, new Path("resource/index.htm"),
				getResource("/templates/index.htm"), monitor);
	}

	private InputStream getResource(String string) {
		return this.getClass().getResourceAsStream(string);
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
    private void addFileToProject(IContainer container, Path path,
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
