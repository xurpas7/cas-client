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
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.constants.RecordStatus;
import org.rmj.appdriver.iface.GEntity;

@Entity
@Table(name="Client_Master")

public class UnitClientMaster implements Serializable, GEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "sClientID")
    private String sClientID;
    
    @Column(name = "cClientTp")
    private String cClientTp;
    
    @Column(name = "sLastName")
    private String sLastName;
    
    @Column(name = "sFrstName")
    private String sFrstName;
    
    @Column(name = "sMiddName")
    private String sMiddName;
    
    @Column(name = "sSuffixNm")
    private String sSuffixNm;
    
    @Column(name = "sClientNm")
    private String sClientNm;
    
    @Column(name = "cGenderCd")
    private String cGenderCd;
    
    @Column(name = "cCvilStat")
    private String cCvilStat;
    
    @Column(name = "sCitizenx")
    private String sCitizenx;
    
    @Column(name = "dBirthDte")
    @Temporal(TemporalType.DATE)
    private Date dBirthDte;
    
    @Column(name = "sBirthPlc")
    private String sBirthPlc;
    
    @Column(name = "sAddlInfo")
    private String sAddlInfo;
    
    @Column(name = "sSpouseID")
    private String sSpouseID;
    
    @Column(name = "cLRClient")
    private String cLRClient;
    
    @Column(name = "cMCClient")
    private String cMCClient;
    
    @Column(name = "cSCClient")
    private String cSCClient;
    
    @Column(name = "cSPClient")
    private String cSPClient;
    
    @Column(name = "cCPClient")
    private String cCPClient;
            
    @Column(name = "cRecdStat")
    private String cRecdStat;   
    
    @Column(name = "sModified")
    private String sModified;   
    
    @Basic(optional = false)
    @Column(name = "dModified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dModified;

    LinkedList laColumns = null;
    
    public UnitClientMaster(){
        this.sClientID = "";
        this.cClientTp = "1";
        this.sLastName = "";
        this.sFrstName = "";
        this.sMiddName = "";
        this.sSuffixNm = "";
        this.dBirthDte = CommonUtils.toDate("1900-01-01");
        this.cGenderCd = "0";
        this.cCvilStat = "0";
        this.sSpouseID = "";
        this.cLRClient = "0";
        this.cMCClient = "0";
        this.cSCClient = "0";
        this.cSPClient = "0";
        this.cCPClient = "0";
        this.cRecdStat = RecordStatus.ACTIVE;
        
        laColumns = new LinkedList();
        laColumns.add("sClientID");
        laColumns.add("cClientTp");
        laColumns.add("sLastName");
        laColumns.add("sFrstName");
        laColumns.add("sMiddName");
        laColumns.add("sSuffixNm");
        laColumns.add("sClientNm");
        laColumns.add("cGenderCd");
        laColumns.add("cCvilStat");
        laColumns.add("sCitizenx");
        laColumns.add("dBirthDte");
        laColumns.add("sBirthPlc");
        laColumns.add("sAddlInfo");
        laColumns.add("sSpouseID");
        laColumns.add("cLRClient");
        laColumns.add("cMCClient");
        laColumns.add("cSCClient");
        laColumns.add("cSPClient");
        laColumns.add("cCPClient");
        laColumns.add("cRecdStat");
        laColumns.add("sModified");
        laColumns.add("dModified");
    }
    
    public void setClientID(String sClientID){
        this.sClientID = sClientID;
    }
    public String getClientID(){
        return sClientID;
    }
    
    public void setClientType(String cClientTp){
        this.cClientTp = cClientTp;
    }
    public String getClientType(){
        return cClientTp;
    }
    
    public void setLastName(String sLastName){
        this.sLastName = sLastName;
        
        this.sClientNm = this.sLastName + ", " + 
                            this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                            this.sMiddName;
    }
    public String getLastName(){
        return sLastName;
    }
    
    public void setFirstName(String sFrstName){
        this.sFrstName = sFrstName;
        
        this.sClientNm = this.sLastName + ", " + 
                            this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                            this.sMiddName;
    }
    public String getFirstName(){
        return sFrstName + (sSuffixNm.length() == 0 ? "" : " " + sSuffixNm);
    }
    
    public void setMiddName(String sMiddName){
        this.sMiddName = sMiddName;
        
        this.sClientNm = this.sLastName + ", " + 
                            this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                            this.sMiddName;
    }
    public String getMiddName(){
        return sMiddName;
    }
    
    public void setSuffixName(String sSuffixNm){
        this.sSuffixNm = sSuffixNm;
        
        this.sClientNm = this.sLastName + ", " + 
                            this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                            this.sMiddName;
    }
    public String getSuffixName(){
        return sSuffixNm;
    }
    
    public void setClientName(String sClientNm){
        this.sClientNm = sClientNm;
    }
    
    public String getClientName(){
        return sClientNm;
    }
    
    public void setGenderCode(String cGenderCd){
        this.cGenderCd = cGenderCd;
    }
    public String getGenderCode(){
        return cGenderCd;
    }
    
    public void setCivilStat(String cCvilStat){
        this.cCvilStat = cCvilStat;
    }
    public String getCivilStat(){
        return cCvilStat;
    }
    
    public void setCitizenship(String sCitizenx){
        this.sCitizenx = sCitizenx;
    }
    public String getCitizenship(){
        return sCitizenx;
    }
    
    public void setBirthDate(Date dBirthDte){
        this.dBirthDte = dBirthDte;
    }
    public Date getBirthDate(){
        return dBirthDte;
    }
    
    public void setBirthPlace(String sBirthPlc){
        this.sBirthPlc = sBirthPlc;
    }
    public String getBirthPlace(){
        return sBirthPlc;
    }
    
    public void setAddtlInfo(String sAddlInfo){
        this.sAddlInfo = sAddlInfo;
    }
    public String getAddtlInfo(){
        return sAddlInfo;
    }
    
    public void setSpouseID(String sSpouseID){
        this.sSpouseID = sSpouseID;
    }
    public String getSpouseID(){
        return sSpouseID;
    }
    
    public void setIsLRClient(String cLRClient){
        this.cLRClient = cLRClient;
    }
    public String getIsLRClient(){
        return cLRClient;
    }
    
    public void setIsMCClient(String cMCClient){
        this.cMCClient = cMCClient;
    }
    public String getIsMCClient(){
        return cMCClient;
    }
    
    public void setIsSCClient(String cSCClient){
        this.cSCClient = cSCClient;
    }
    public String getIsSCClient(){
        return cSCClient;
    }
    
    public void setIsSPClient(String cSPClient){
        this.cSPClient = cSPClient;
    }
    public String setIsSPClient(){
        return cSPClient;
    }
    
    public void setIsCPCLient(String cCPClient){
        this.cCPClient = cCPClient;
    }
    public String getIsCPCLient(){
        return cCPClient;
    }
       
    public void setRecordStat(String cRecdStat){
        this.cRecdStat = cRecdStat;
    }
    public String getRecordStat(){
        return cRecdStat;
    }
    
    public void setModifiedBy(String sModified){
        this.sModified = sModified;
    }
    public String getModifiedBy(){
        return sModified;
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
        if (!(object instanceof UnitClientMaster)) {
            return false;
        }
        UnitClientMaster other = (UnitClientMaster) object;
        if ((this.sClientID == null && other.sClientID != null) || (this.sClientID != null && !this.sClientID.equals(other.sClientID))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.rmj.clientfx.pojo.UnitClientMaster[sClientID=" + sClientID + "]";
    }
    
    @Override
    public Object getValue(int fnColumn) {
        switch(fnColumn){
            case 1: return sClientID;
            case 2: return cClientTp;
            case 3: return sLastName;
            case 4: return sFrstName;
            case 5: return sMiddName;
            case 6: return sSuffixNm;
            case 7: return sClientNm;
            case 8: return cGenderCd;
            case 9: return cCvilStat;
            case 10: return sCitizenx;
            case 11: return dBirthDte;
            case 12: return sBirthPlc;
            case 13: return sAddlInfo;
            case 14: return sSpouseID;
            case 15: return cLRClient;
            case 16: return cMCClient;
            case 17: return cSCClient;
            case 18: return cSPClient;
            case 19: return cCPClient;
            case 20: return cRecdStat;
            case 21: return sModified;
            case 22: return dModified;
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
        return "Client_Master";
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
            case 2: cClientTp = (String) foValue; break;
            case 3: sLastName = (String) foValue; 
                    this.sClientNm = this.sLastName + ", " + 
                    this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                    this.sMiddName;
                    
                    break;
            case 4: sFrstName = (String) foValue;                    
                    this.sClientNm = this.sLastName + ", " + 
                    this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                    this.sMiddName;
                    
                    break;
            case 5: sMiddName = (String) foValue; 
                    this.sClientNm = this.sLastName + ", " + 
                    this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                    this.sMiddName;
                    
                    break;
            case 6: sSuffixNm = (String) foValue;                    
                    this.sClientNm = this.sLastName + ", " + 
                    this.sFrstName + " " + (sSuffixNm.length() == 0 ? "" : sSuffixNm) + " " + 
                    this.sMiddName;
                    
                    break;
            case 7: if (this.cCPClient.equals("0")) this.sClientNm = (String) foValue; break;
            case 8: cGenderCd = (String) foValue; break;
            case 9: cCvilStat = (String) foValue; break;
            case 10: sCitizenx = (String) foValue; break;
            case 11: dBirthDte = (Date) foValue; break;
            case 12: sBirthPlc = (String) foValue; break;
            case 13: sAddlInfo = (String) foValue; break;
            case 14: sSpouseID = (String) foValue; break;
            case 15: cLRClient = (String) foValue; break;
            case 16: cMCClient = (String) foValue; break;
            case 17: cSCClient = (String) foValue; break;
            case 18: cSPClient = (String) foValue; break;
            case 19: cCPClient = (String) foValue; break;
            case 20: cRecdStat = (String) foValue; break;
            case 21: sModified = (String) foValue; break;
            case 22: dModified = (Date) foValue; break;
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