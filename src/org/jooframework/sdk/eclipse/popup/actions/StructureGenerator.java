package org.jooframework.sdk.eclipse.popup.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jooframework.sdk.eclipse.JooSDK;

public class StructureGenerator implements IObjectActionDelegate {

	private Shell shell;
	private IWorkbenchPart part;
	
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
		IFolder folder = (IFolder)selection.getFirstElement();
		
		try {
			JooSDK.createDefaultStructure(folder, null);
		} catch (Exception ex) {
			MessageDialog.openError(
					shell,
					"Joo SDK Error",
					"Error while generating file structure: "+ex.toString());
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
