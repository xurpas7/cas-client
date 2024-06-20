/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import javafx.beans.property.SimpleStringProperty;

public class MobileNumber {    
        private SimpleStringProperty entryno;
        private SimpleStringProperty mobile;
        private SimpleStringProperty priority;       
 
        MobileNumber(String entryno, String mobile, String priority) {
            this.entryno = new SimpleStringProperty(entryno);
            this.mobile = new SimpleStringProperty(mobile);
            this.priority= new SimpleStringProperty(priority);            
        }
         
    public String getEntryno() {
        return entryno.get();
    }
    public void setEntryno(String id) {
        this.entryno.set(id);
    }

    public String getMobile() {
        return mobile.get();
    }

    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public String getPriority() {
        return priority.get();
    }
    public void setPriority(String age) {
        this.priority.set(age);
    }
}
