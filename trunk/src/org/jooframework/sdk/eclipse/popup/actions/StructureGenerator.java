package org.jooframework.sdk.eclipse.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jooframework.sdk.eclipse.JooSDK;

public class StructureGenerator implements IObjectActionDelegate {

	private Shell shell;
	private IWorkbenchPart part;
	private IContainer folder;
	
	/**
	 * Constructor for Action1.
	 */
	public StructureGenerator() {
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
		folder = (IContainer)selection.getFirstElement();
		
		Job job = new Job("Generate Joo default structure") {
			@Override
			protected IStatus run(IProgressMonitor arg0) {
				try {
					JooSDK.createDefaultStructure(folder, null);
				} catch (Exception ex) {
					//MessageDialog.openError(
						//	shell,
							//"Joo SDK Error",
							//"Error while generating file structure: "+ex.toString());
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

}
