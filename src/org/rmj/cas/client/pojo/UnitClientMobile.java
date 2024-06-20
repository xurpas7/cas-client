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
@Table(name="Client_Mobile")

public class UnitClientMobile implements Serializable, GEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sClientID")
    private String sClientID;
    
    @Column(name = "nEntryNox")
    private int nEntryNox;
   
    @Id
    @Basic(optional = false)
    @Column(name = "sMobileNo")
    private String sMobileNo;
    
    @Column(name = "nPriority")
    private int nPriority;
    
    @Column(name = "cIncdMktg")
    private String cIncdMktg;
    
    @Column(name = "nUnreachx")
    private int nUnreachx;
    
    @Column(name = "dLastVeri")
    @Temporal(TemporalType.DATE)
    private Date dLastVeri;
    
    @Column(name = "dInactive")
    @Temporal(TemporalType.DATE)
    private Date dInactive;
    
    @Column(name = "nNoRetryx")
    private int nNoRetryx;
    
    @Column(name = "cInvalidx")
    private String cInvalidx;
    
    @Column(name = "cConfirmd")
    private String cConfirmd;
    
    @Column(name = "dConfirmd")
    @Temporal(TemporalType.DATE)
    private Date dConfirmd;
    
    @Column(name = "cSubscrbr")
    private String cSubscrbr;
    
    @Column(name = "dHoldMktg")
    @Temporal(TemporalType.DATE)
    private Date dHoldMktg;
    
    @Column(name = "dMktgMsg1")
    @Temporal(TemporalType.DATE)
    private Date dMktgMsg1;
    
    @Column(name = "cNewMobil")
    private String cNewMobil;
    
    @Column(name = "dMktgMsg2")
    @Temporal(TemporalType.DATE)
    private Date dMktgMsg2;
    
    @Column(name = "dMktgMsg3")
    @Temporal(TemporalType.DATE)
    private Date dMktgMsg3;
    
    @Column(name = "cRecdStat")
    private String cRecdStat;   
    @Basic(optional = false)
    @Column(name = "dModified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dModified;

    LinkedList laColumns = null;
    
    public UnitClientMobile(){
        this.sClientID = "";
        this.sMobileNo = "";
        this.cIncdMktg = "1";
        this.nEntryNox = -1;
        this.nPriority = -1;
        this.cRecdStat = RecordStatus.ACTIVE;
        
        laColumns = new LinkedList();
        laColumns.add("sClientID");
        laColumns.add("nEntryNox");
        laColumns.add("sMobileNo");
        laColumns.add("nPriority");
        laColumns.add("cIncdMktg");
        laColumns.add("nUnreachx");
        laColumns.add("dLastVeri");
        laColumns.add("dInactive");
        laColumns.add("nNoRetryx");
        laColumns.add("cInvalidx");
        laColumns.add("cConfirmd");
        laColumns.add("dConfirmd");
        laColumns.add("cSubscrbr");
        laColumns.add("dHoldMktg");
        laColumns.add("dMktgMsg1");
        laColumns.add("cNewMobil");
        laColumns.add("dMktgMsg2");
        laColumns.add("dMktgMsg3");
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
    
    public void setMobileNo(String sMobileNo){
        this.sMobileNo = sMobileNo;
    }
    public String getMobileNo(){
        return sMobileNo;
    }
    
    public void setPriority(int nPriority){
        this.nPriority = nPriority;
    }
    public int getPriority(){
        return nPriority;
    }
    
    public void setIncldMktg(String cIncdMktg){
        this.cIncdMktg = cIncdMktg;
    }
    public String getIncldMktg(){
        return cIncdMktg;
    }
    
    public void setUnreachable(int nUnreachx){
        this.nUnreachx = nUnreachx;
    }
    public int getUnreachable(){
        return nUnreachx;
    }
    
    public void setDateLastVeri(Date dLastVeri){
        this.dLastVeri = dLastVeri;
    }
    public Date getDateLastVeri(){
        return dLastVeri;
    }
    
    public void setDateInactive(Date dInactive){
        this.dInactive = dInactive;
    }
    public Date getDateInactive(){
        return dInactive;
    }
    
    public void setNoOfRetry(int nNoRetryx){
        this.nNoRetryx = nNoRetryx;
    }
    public int getNoOfRetry(){
        return nNoRetryx;
    }
    
    public void setIsInvalid(String cInvalidx){
        this.cInvalidx = cInvalidx;
    }
    public String getIsInvalid(){
        return cInvalidx;
    }
    
    public void setIsConfirmed(String cConfirmd){
        this.cConfirmd = cConfirmd;
    }
    public String getIsConfirmed(){
        return cConfirmd;
    }
    
    public void setDateConfirmed(Date dConfirmd){
        this.dConfirmd = dConfirmd;
    }
    public Date getDateConfirmed(){
        return dConfirmd;
    }
    
    public void setSubscriber(String cSubscrbr){
        this.cSubscrbr = cSubscrbr;
    }
    public String getSubscriber(){
        return cSubscrbr;
    }
    
    public void setDateHoldMktg(Date dHoldMktg){
        this.dHoldMktg = dHoldMktg;
    }
    public Date getDateHoldMktg(){
        return dHoldMktg;
    }
    
    public void setDateMktgMsg1(Date dMktgMsg1){
        this.dMktgMsg1 = dMktgMsg1;
    }
    public Date getDateMktgMsg1(){
        return dMktgMsg1;
    }
    
    public void setIsNewMobile(String cNewMobil){
        this.cNewMobil = cNewMobil;
    }
    public String getIsNewMobile(){
        return cNewMobil;
    }
    
    public void setDateMktgMsg2(Date dMktgMsg2){
        this.dMktgMsg2 = dMktgMsg2;
    }
    public Date getDateMktgMsg2(){
        return dMktgMsg2;
    }
    
    public void setDateMktgMsg3(Date dMktgMsg3){
        this.dMktgMsg3 = dMktgMsg3;
    }
    public Date getDateMktgMsg3(){
        return dMktgMsg3;
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
        if (!(object instanceof UnitClientMobile)) {
            return false;
        }
        UnitClientMobile other = (UnitClientMobile) object;
        if ((this.sClientID == null && other.sClientID != null) || (this.sClientID != null && !this.sClientID.equals(other.sClientID))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.rmj.clientfx.pojo.UnitClientMobile[sClientID=" + sClientID + " AND sMobileNo=" + sMobileNo + "]";
    }
    
    @Override
    public Object getValue(int fnColumn) {
        switch(fnColumn){
            case 1: return sClientID;
            case 2: return nEntryNox;
            case 3: return sMobileNo;
            case 4: return nPriority;
            case 5: return cIncdMktg;
            case 6: return nUnreachx;
            case 7: return dLastVeri;
            case 8: return dInactive;
            case 9: return nNoRetryx;
            case 10: return cInvalidx;
            case 11: return cConfirmd;
            case 12: return dConfirmd;
            case 13: return cSubscrbr;
            case 14: return dHoldMktg;
            case 15: return dMktgMsg1;
            case 16: return cNewMobil;
            case 17: return dMktgMsg2;
            case 18: return dMktgMsg3;
            case 19: return cRecdStat;
            case 20: return dModified;
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
        return "Client_Mobile";
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
            case 3: sMobileNo = (String) foValue; break;
            case 4: nPriority = (int) foValue; break;
            case 5: cIncdMktg = (String) foValue; break;
            case 6: nUnreachx = (int) foValue; break;
            case 7: dLastVeri = (Date) foValue; break;
            case 8: dInactive = (Date) foValue; break;
            case 9: nNoRetryx = (int) foValue; break;
            case 10: cInvalidx = (String) foValue; break;
            case 11: cConfirmd = (String) foValue; break;
            case 12: dConfirmd = (Date) foValue; break;
            case 13: cSubscrbr = (String) foValue; break;
            case 14: dHoldMktg = (Date) foValue; break;
            case 15: dMktgMsg1 = (Date) foValue; break;
            case 16: cNewMobil = (String) foValue; break;
            case 17: dMktgMsg2 = (Date) foValue; break;
            case 18: dMktgMsg3 = (Date) foValue; break;
            case 19: cRecdStat = (String) foValue; break;
            case 20: dModified = (Date) foValue; break;
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