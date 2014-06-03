package org.jooframework.sdk.eclipse.popup;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CredentialsDialog extends TitleAreaDialog {

	private Text txtUsernameInput;
	private Text txtPasswordInput;

	private String username;
	private String password;
	private String comp;

	public CredentialsDialog(Shell parentShell, String comp) {
		super(parentShell);
		this.comp = comp;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Enter Cerberus Credentials");
		setMessage("The feature you are using requires Cerberus username and password. The account must have access to "+comp, IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createFirstName(container);
		createLastName(container);

		return area;
	}

	private void createFirstName(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Username");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		txtUsernameInput = new Text(container, SWT.BORDER);
		txtUsernameInput.setLayoutData(dataFirstName);
	}

	private void createLastName(Composite container) {
		Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Password");

		GridData dataLastName = new GridData();
		dataLastName.grabExcessHorizontalSpace = true;
		dataLastName.horizontalAlignment = GridData.FILL;
		txtPasswordInput = new Text(container, SWT.BORDER);
		txtPasswordInput.setLayoutData(dataLastName);
		txtPasswordInput.setEchoChar('*');
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		username = txtUsernameInput.getText();
		password = txtPasswordInput.getText();

	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}