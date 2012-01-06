package org.jooframework.sdk.eclipse.popup.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IResourceActionFilter;

public class RootFolderFilter implements IResourceActionFilter {

	public boolean testAttribute(Object object, String name, String value) {
		if (object instanceof IFolder) {
			IFolder f = (IFolder) object;
			try {
				IResource []members = f.members();
				for(IResource member: members) {
					if (member instanceof IFile) {
						String fname = ((IFile)member).getName();
						if (fname.startsWith("index.") && fname.endsWith(".copy.html"))
							return true;
					}
				}
			} catch (CoreException e) {
				
			}
		}
		MessageDialog.openError(null, "not found", "not found");
		return true;
	}

}
