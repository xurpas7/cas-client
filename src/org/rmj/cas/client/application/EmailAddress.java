/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import javafx.beans.property.SimpleStringProperty;

public class EmailAddress {    
        private SimpleStringProperty entryno;
        private SimpleStringProperty email;
        private SimpleStringProperty priority;       
 
        EmailAddress(String entryno, String email, String priority) {
            this.entryno = new SimpleStringProperty(entryno);
            this.email = new SimpleStringProperty(email);
            this.priority= new SimpleStringProperty(priority);            
        }
         
    public String getEntryno() {
        return entryno.get();
    }
    public void setEntryno(String id) {
        this.entryno.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPriority() {
        return priority.get();
    }
    public void setPriority(String age) {
        this.priority.set(age);
    }
}
