/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import javafx.beans.property.SimpleStringProperty;

public class Address {    
    private SimpleStringProperty entryno;
    private SimpleStringProperty address;        
    private SimpleStringProperty priority;       

    Address(String entryno, String address, String priority) {
        this.entryno = new SimpleStringProperty(entryno);
        this.address = new SimpleStringProperty(address);
        this.priority= new SimpleStringProperty(priority);            
    }
         
    public String getEntryno() {
        return entryno.get();
    }
    public void setAddress(String id) {
        this.entryno.set(id);
    }

    public String getAddress() {
        return address.get();
    }

    public void setHouseno(String address) {
        this.address.set(address);
    }

    public String getPriority() {
        return priority.get();
    }
    public void setPriority(String age) {
        this.priority.set(age);
    }
}
