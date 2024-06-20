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
@Table(name="Client_Address")

public class UnitClientAddress implements Serializable, GEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sClientID")
    private String sClientID;
    
    @Column(name = "nEntryNox")
    private int nEntryNox;
   
    @Id
    @Basic(optional = false)
    @Column(name = "sHouseNox")
    private String sHouseNox;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sAddressx")
    private String sAddressx;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sBrgyIDxx")
    private String sBrgyIDxx;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sTownIDxx")
    private String sTownIDxx;
    
    @Column(name = "nPriority")
    private int nPriority;
    
    @Column(name = "nLatitude")
    private Number nLatitude;
    
    @Column(name = "nLongitud")
    private Number nLongitud;
    
    @Column(name = "cPrimaryx")
    private String cPrimaryx;
    
    @Column(name = "cRecdStat")
    private String cRecdStat;
    
    @Column(name = "dModified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dModified;

    LinkedList laColumns = null;
    
    public UnitClientAddress(){
        this.sClientID = "";
        this.sHouseNox = "";
        this.sAddressx = "";
        this.sBrgyIDxx = "";
        this.sTownIDxx = "";
        this.cPrimaryx = "0";
        this.nEntryNox = -1;
        this.nPriority = -1;
        this.cRecdStat = RecordStatus.ACTIVE;
        
        laColumns = new LinkedList();
        laColumns.add("sClientID");
        laColumns.add("nEntryNox");
        laColumns.add("sHouseNox");
        laColumns.add("sAddressx");
        laColumns.add("sBrgyIDxx");
        laColumns.add("sTownIDxx");
        laColumns.add("nPriority");
        laColumns.add("nLatitude");
        laColumns.add("nLongitud");
        laColumns.add("cPrimaryx");
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
    
    public void setHouseNo(String sHouseNox){
        this.sHouseNox = sHouseNox;
    }
    public String getHouseNo(){
        return sHouseNox;
    }
    
    public void setAddress(String sAddressx){
        this.sAddressx = sAddressx;
    }
    public String getAddress(){
        return sAddressx;
    }
    
    public void setBrgyID(String sBrgyIDxx){
        this.sBrgyIDxx = sBrgyIDxx;
    }
    public String getBrgyID(){
        return sBrgyIDxx;
    }
    
    public void setTownID(String sTownIDxx){
        this.sTownIDxx = sTownIDxx;
    }
    public String getTownID(){
        return sTownIDxx;
    }
    
    public void setPriority(int nPriority){
        this.nPriority = nPriority;
    }
    public int getPriority(){
        return nPriority;
    }
    
    public void setLatitude(Number nLatitude){
        this.nLatitude = nLatitude;
    }
    public Number getLatitude(){
        return nLatitude;
    }
    
    public void setLongitude(Number nLongitud){
        this.nLongitud = nLongitud;
    }
    public Number getLongitude(){
        return nLongitud;
    }
       
    public void setRecordStat(String cRecdStat){
        this.cRecdStat = cRecdStat;
    }
    public String getRecordStat(){
        return cRecdStat;
    }
    
    public void setIsPrimary(String cPrimaryx){
        this.cPrimaryx = cPrimaryx;
    }
    public String getIsPrimary(){
        return cPrimaryx;
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
        if (!(object instanceof UnitClientAddress)) {
            return false;
        }
        UnitClientAddress other = (UnitClientAddress) object;
        if ((this.sClientID == null && other.sClientID != null) || (this.sClientID != null && !this.sClientID.equals(other.sClientID))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.rmj.clientfx.pojo.UnitClientAddress[sClientID=" + sClientID + 
                                                        " AND sHouseNox =" + sHouseNox + 
                                                        " AND sAddressx =" + sAddressx + 
                                                        " AND sBrgyIDxx =" + sBrgyIDxx + 
                                                        " AND sTownIDxx =" + sTownIDxx + 
                                                        "]";
    }
    
    @Override
    public Object getValue(int fnColumn) {
        switch(fnColumn){
            case 1: return sClientID;
            case 2: return nEntryNox;
            case 3: return sHouseNox;
            case 4: return sAddressx;
            case 5: return sBrgyIDxx;
            case 6: return sTownIDxx;
            case 7: return nPriority;
            case 8: return nLatitude;
            case 9: return nLongitud;
            case 10: return cPrimaryx;
            case 11: return cRecdStat;
            case 12: return dModified;
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
        return "Client_Address";
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
            case 3: sHouseNox = (String) foValue; break;
            case 4: sAddressx = (String) foValue; break;
            case 5: sBrgyIDxx = (String) foValue; break;
            case 6: sTownIDxx = (String) foValue; break;
            case 7: nPriority = (int) foValue; break;
            case 8: nLatitude = (Number) foValue; break;
            case 9: nLongitud = (Number) foValue; break;
            case 10: cPrimaryx = (String) foValue; break;
            case 11: cRecdStat = (String) foValue; break;
            case 12: dModified = (Date) foValue; break;
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