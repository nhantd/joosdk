package org.jooframework.sdk.eclipse.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jooframework.sdk.eclipse.JooSDK;
import org.jooframework.sdk.eclipse.popup.filters.RootFolderFilter;

public class CatComponents implements IObjectActionDelegate, IAdaptable {

	private Shell shell;
	private IWorkbenchPart part;
	private IResource res;
	
	/**
	 * Constructor for Action1.
	 */
	public CatComponents() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		part = targetPart;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IStructuredSelection selection = (IStructuredSelection) part.getSite().getSelectionProvider().getSelection();
		res = (IResource)selection.getFirstElement();
		
		Job job = new Job("Update CAT components") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					if (res instanceof IContainer) {
						JooSDK.updateCat((IContainer)res);
					} else {
						JooSDK.updateCat((IFile)res);
					}
				} catch (Exception ex) {
					
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	public Object getAdapter(Class c) {
		if (c == IActionFilter.class) {
			return new RootFolderFilter();
		}
		return null;
	}

}
