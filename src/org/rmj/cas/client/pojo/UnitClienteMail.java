/**
 * @author  Michael Cuison
 */
package org.rmj.cas.client.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.rmj.appdriver.constants.RecordStatus;
import org.rmj.appdriver.iface.GEntity;

@Entity
@Table(name="Client_eMail_Address")

public class UnitClienteMail implements Serializable, GEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sClientID")
    private String sClientID;
    
    @Column(name = "nEntryNox")
    private int nEntryNox;
   
    @Id
    @Basic(optional = false)
    @Column(name = "sEMailAdd")
    private String sEMailAdd;
    
    @Column(name = "nPriority")
    private int nPriority;
    
    @Column(name = "cRecdStat")
    private String cRecdStat;
    
    @Column(name = "dModified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dModified;

    LinkedList laColumns = null;
    
    public UnitClienteMail(){
        this.sClientID = "";
        this.sEMailAdd = "";
        this.nEntryNox = -1;
        this.nPriority = -1;
        this.cRecdStat = RecordStatus.ACTIVE;
        
        laColumns = new LinkedList();
        laColumns.add("sClientID");
        laColumns.add("nEntryNox");
        laColumns.add("sEMailAdd");
        laColumns.add("nPriority");
        laColumns.add("cRecdStat");
        laColumns.add("dModified");
    }
    
    public void setClientID(String sClientID){
        this.sClientID = sClientID;
    }
    public String getClientID(){
        return sClientID;
    }
    
    public void setEntryNo(int nEntryNox){
        this.nEntryNox = nEntryNox;
    }
    public int getEntryNo(){
        return nEntryNox;
    }
    
    public void setEMailAdd(String sEMailAdd){
        this.sEMailAdd = sEMailAdd;
    }
    public String getEMailAdd(){
        return sEMailAdd;
    }
    
    public void setPriority(int nPriority){
        this.nPriority = nPriority;
    }
    public int getPriority(){
        return nPriority;
    }
       
    public void setRecordStat(String cRecdStat){
        this.cRecdStat = cRecdStat;
    }
    public String getRecordStat(){
        return cRecdStat;
    }
    
    public void setDateModified(Date dModified){
        this.dModified = dModified;
    }
    public Date getDateModified(){
        return dModified;
    }
    
    @Override
    public int hashCode(){
        int hash = 0;
        hash += (sClientID != null ? sClientID.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnitClienteMail)) {
            return false;
        }
        UnitClienteMail other = (UnitClienteMail) object;
        if ((this.sClientID == null && other.sClientID != null) || (this.sClientID != null && !this.sClientID.equals(other.sClientID))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.rmj.clientfx.pojo.UnitClienteMail[sClientID=" + sClientID + " AND sEMailAdd=" + sEMailAdd + "]";
    }
    
    @Override
    public Object getValue(int fnColumn) {
        switch(fnColumn){
            case 1: return sClientID;
            case 2: return nEntryNox;
            case 3: return sEMailAdd;
            case 4: return nPriority;
            case 5: return cRecdStat;
            case 6: return dModified;
            default: return null;
        }
    }

    @Override
    public Object getValue(String fsColumn) {
        int lnCol = getColumn(fsColumn);
        
        if (lnCol > 0){
            return getValue(lnCol);
        } else
            return null;
    }

    @Override
    public String getTable() {
        return "Client_eMail_Address";
    }

    @Override
    public String getColumn(int fnCol) {
        if (laColumns.size() < fnCol){
            return "";
        } else 
            return (String) laColumns.get(fnCol - 1);
    }

    @Override
    public int getColumn(String fsCol) {
        return laColumns.indexOf(fsCol) + 1;
    }

    @Override
    public void setValue(int fnColumn, Object foValue) {
        switch(fnColumn){
            case 1: sClientID = (String) foValue; break;
            case 2: nEntryNox = (int) foValue; break;
            case 3: sEMailAdd = (String) foValue; break;
            case 4: nPriority = (int) foValue; break;
            case 5: cRecdStat = (String) foValue; break;
            case 6: dModified = (Date) foValue; break;
        }    
    }

    @Override
    public void setValue(String fsColumn, Object foValue) {
        int lnCol = getColumn(fsColumn);
        if (lnCol > 0){
            setValue(lnCol, foValue);
        }
    }

    @Override
    public int getColumnCount() {
        return laColumns.size();
    }

    @Override
    public void list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}