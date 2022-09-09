package com.application.library.vaadin;

import com.application.library.account.ReaderModel;
import com.application.library.account.ReaderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("sign_up")
public class RegisterView extends VerticalLayout {
    TextField nameText;
    PasswordField passwordText;
    EmailField emailText;
    Button registerButton;
    private final ReaderService service;

    public RegisterView(ReaderService service){
        this.service = service;
        nameText = new TextField("Nickname");
        passwordText = new PasswordField("Password");
        emailText = new EmailField("Email");
        registerButton = new Button("Register");

        registerButton.addClickListener(ClickEvent -> addAccount());

        add(nameText);
        add(passwordText);
        add(emailText);
        add(registerButton);

    }

    private void addAccount() {
        ReaderModel account = new ReaderModel();
        account.setName(nameText.getValue());
        account.setPassword(passwordText.getValue());
        account.setEmail(emailText.getValue());
        service.addAccount(account);
        Notification.show("Account was added");
    }

}
