/**
 * @author  Michael Cuison
 */
package org.rmj.cas.client.base;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.rmj.appdriver.GCrypt;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.iface.GEntity;
import org.rmj.appdriver.iface.GTransaction;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.client.pojo.UnitClientAddress;
import org.rmj.cas.client.pojo.UnitClientMaster;
import org.rmj.cas.client.pojo.UnitClientMobile;
import org.rmj.cas.client.pojo.UnitClienteMail;

public class Client implements GTransaction{
    @Override
    public UnitClientMaster newTransaction() {
        UnitClientMaster loObj = new UnitClientMaster();
        Connection loConn = null;
        loConn = setConnection();
        
        if (poGRider.getProductID()=="Telecom"){
            loObj.setClientID(MiscUtil.getNextCode(loObj.getTable(), "sClientID", true, loConn, "X" + psBranchCd.substring(1, 4)));
        }else{
            loObj.setClientID(MiscUtil.getNextCode(loObj.getTable(), "sClientID", true, loConn, "Y" + psBranchCd.substring(1, 4)));  
        } 
        
        //init detail
        poMobile = new ArrayList<>();
        poEmail = new ArrayList<>();
        poAddress = new ArrayList<>();
        
        return loObj;
    }

    @Override
    public UnitClientMaster loadTransaction(String fsTransNox) {
        UnitClientMaster loObject = new UnitClientMaster();
        
        Connection loConn = null;
        loConn = setConnection();   
        
        String lsSQL = MiscUtil.addCondition(getSQ_Master(), "sClientID = " + SQLUtil.toSQL(fsTransNox));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        try {
            if (!loRS.next()){
                setMessage("No Record Found");
            }else{
                //load each column to the entity
                for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                    loObject.setValue(lnCol, loRS.getObject(lnCol));
                }
                
                //load detail
                poMobile = loadClientMobile(fsTransNox);
            }              
        } catch (SQLException ex) {
            setErrMsg(ex.getMessage());
        } finally{
            MiscUtil.close(loRS);
            if (!pbWithParent) MiscUtil.close(loConn);
        }
        
        setMessage("Record Save Successfully.");
        return loObject;
    }

    @Override
    public UnitClientMaster saveUpdate(Object foEntity, String fsTransNox) {
        String lsSQL = "";
        
        UnitClientMaster loOldEnt = null;
        UnitClientMaster loNewEnt = null;
        UnitClientMaster loResult = null;
        
        // Check for the value of foEntity
        if (!(foEntity instanceof UnitClientMaster)) {
            setErrMsg("Invalid Entity Passed as Parameter");
            return loResult;
        }
        
        // Typecast the Entity to this object
        loNewEnt = (UnitClientMaster) foEntity;
        
        
        // Test if entry is ok
        if (loNewEnt.getClientID() == null || loNewEnt.getClientID().isEmpty()){
            setMessage("Invalid client detected.");
            return loResult;
        }
        
        if (loNewEnt.getLastName() == null || loNewEnt.getLastName().isEmpty()){
            setMessage("Invalid last name detected.");
            return loResult;
        }
        
        if (loNewEnt.getFirstName()== null || loNewEnt.getFirstName().isEmpty()){
            setMessage("Invalid first name detected.");
            return loResult;
        }
        
        if (loNewEnt.getMiddName()== null || loNewEnt.getMiddName().isEmpty()){
            setMessage("Invalid middle name detected.");
            return loResult;
        }
        
        if (loNewEnt.getBirthDate()== null){
            setMessage("Invalid birth date detected.");
            return loResult;
        }
        
        if (loNewEnt.getBirthPlace()== null || loNewEnt.getBirthPlace().isEmpty()){
            setMessage("Invalid birth place detected.");
            return loResult;
        }
        
        if (!pbWithParent) poGRider.beginTrans();
        
        // Generate the SQL Statement
        if (fsTransNox.equals("")){
            Connection loConn = null;
            loConn = setConnection();

            String lsTransNox = MiscUtil.getNextCode(loNewEnt.getTable(), "sClientID", false, loConn, psBranchCd);
  
            loNewEnt.setModifiedBy(poGRider.getUserID());
            loNewEnt.setDateModified(poGRider.getServerDate());

            if (!pbWithParent) MiscUtil.close(loConn);

            //Generate the SQL Statement
            fsTransNox = lsTransNox;
            lsSQL = MiscUtil.makeSQL((GEntity) loNewEnt);
        }else{
            //Load previous transaction
            loOldEnt = loadTransaction(fsTransNox);

            loNewEnt.setDateModified(poGRider.getServerDate());

            //Generate the Update Statement
            lsSQL = MiscUtil.makeSQL((GEntity) loNewEnt, (GEntity) loOldEnt, "sClientID = " + SQLUtil.toSQL(loNewEnt.getClientID()));
        }
        
        //No changes have been made
        if (lsSQL.equals("")){
            setMessage("Record is not updated");
            return loResult;
        }
        
        if(poGRider.executeQuery(lsSQL, loNewEnt.getTable(), "", "") == 0){
            if(!poGRider.getErrMsg().isEmpty())
                setErrMsg(poGRider.getErrMsg());
            else setMessage("");
        } else loResult = loNewEnt;
        
        /*Save Other Info*/
        if (!saveOthers(loNewEnt.getClientID())){
            poGRider.rollbackTrans();
            loResult = null;
            return loResult;
        };
        
        if (!pbWithParent) {
            if (!getErrMsg().isEmpty()){
                loResult = null;
                poGRider.rollbackTrans();
                return loResult;
            } else{ 
                poGRider.commitTrans();
            }
        }        
        
        setMessage("Record saved successfully.");
        return loResult;
    }

    private boolean saveOthers(String fsTransNox){
        try {
            if (!saveAddress(fsTransNox, true)) return false;
            if (!saveMobile(fsTransNox, true)) return false;
            saveEmail(fsTransNox, true);
            return true;
        } catch (SQLException ex) {
            setErrMsg(ex.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteTransaction(String fsTransNox) {
        UnitClientMaster loObject = loadTransaction(fsTransNox);
        boolean lbResult = false;
        
        if (loObject == null){
            setMessage("No record found...");
            return lbResult;
        }
        
        String lsSQL = "DELETE FROM " + loObject.getTable() + 
                        " WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox);
        
        if (!pbWithParent) poGRider.beginTrans();
        
        if (poGRider.executeQuery(lsSQL, loObject.getTable(), "", "") == 0){
            if (!poGRider.getErrMsg().isEmpty()){
                setErrMsg(poGRider.getErrMsg());
            } else setErrMsg("No record deleted.");  
        } else lbResult = true;
        
        //delete detail rows
        lsSQL = "DELETE FROM " + pxeMobileTable +
                " WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox);
        
        if (poGRider.executeQuery(lsSQL, pxeMobileTable, "", "") == 0){
            if (!poGRider.getErrMsg().isEmpty()){
                setErrMsg(poGRider.getErrMsg());
            } else setErrMsg("No record deleted.");  
        } else lbResult = true;
        
        if (!pbWithParent){
            if (getErrMsg().isEmpty()){
                poGRider.commitTrans();
            } else poGRider.rollbackTrans();
        }
        
        return lbResult;
    }

    @Override
    public boolean closeTransaction(String fsTransNox) {
        return true;
    }

    @Override
    public boolean postTransaction(String fsTransNox) {
        return true;
    }

    @Override
    public boolean voidTransaction(String fsTransNox) {
        return true;
    }

    @Override
    public boolean cancelTransaction(String fsTransNox) {
        return true;
    }

    @Override
    public String getMessage() {
        return psWarnMsg;
    }

    @Override
    public void setMessage(String fsMessage) {
        this.psWarnMsg = fsMessage;
    }

    @Override
    public String getErrMsg() {
        return psErrMsgx;
    }

    @Override
    public void setErrMsg(String fsErrMsg) {
        this.psErrMsgx = fsErrMsg;
    }

    @Override
    public void setBranch(String foBranchCD) {
        this.psBranchCd = foBranchCD;
    }

    @Override
    public void setWithParent(boolean fbWithParent) {
        this.pbWithParent = fbWithParent;
    }

    @Override
    public String getSQ_Master() {
        return "";
    }
    
    //Added detail methods
    public int MobileCount(){
        return poMobile.size();
    }
    
    public int eMailCount(){
        return poEmail.size();
    }
    
    public int AddressCount(){
        return poAddress.size();
    }
    
    public boolean addMobile() {
        UnitClientMobile loDetail = new UnitClientMobile();
        return poMobile.add(loDetail);
    }
    
    public boolean addEmail() {
        UnitClienteMail loDetail = new UnitClienteMail();
        
        return poEmail.add(loDetail);
    }
    
    public boolean addAddress() {
        UnitClientAddress loDetail = new UnitClientAddress();
        
        return poAddress.add(loDetail);
    }

    public boolean removeMobile(int fnEntryNox) {
        poMobile.remove(fnEntryNox);
        return true;
    }
    
    public boolean removeEmail(int fnEntryNox) {
        poEmail.remove(fnEntryNox);
        return true;
    }
    
    public boolean removeAddress(int fnEntryNox) {
        poAddress.remove(fnEntryNox);
        return true;
    }
    
    public void setMobileInfo(int fnRow, int fnCol, Object foData){
        switch (fnCol){
            case 2: //nEntryNox
            case 4: //nPriority
            case 6: //nUnreachx
            case 9: //nNoRetryx
                if (foData instanceof Integer){
                    poMobile.get(fnRow).setValue(fnCol, foData);
                }else poMobile.get(fnRow).setValue(fnCol, 0);
                break;
            case 7: //dLastVeri
            case 8: //dInactive
            case 12: //dConfirmd
            case 14: //dHoldMktg
            case 15: //dMktgMsg1
            case 17: //dMktgMsg2
            case 18: //dMktgMsg3
                if (foData instanceof Date){
                    poMobile.get(fnRow).setValue(fnCol, foData);
                }else poMobile.get(fnRow).setValue(fnCol, null);
                break;
            default:
                poMobile.get(fnRow).setValue(fnCol, foData);
        }
    }
    public void setMobileInfo(int fnRow, String fsCol, Object foData){
        switch(fsCol){
            case "nEntryNox":
            case "nPriority":
            case "nUnreachx":
            case "nNoRetryx":
                if (foData instanceof Integer){
                    poMobile.get(fnRow).setValue(fsCol, foData);
                }else poMobile.get(fnRow).setValue(fsCol, 0);
                break;
            case "dLastVeri":
            case "dInactive":
            case "dConfirmd":
            case "dHoldMktg":
            case "dMktgMsg1":
            case "dMktgMsg2":
            case "dMktgMsg3":
                if (foData instanceof Date){
                    poMobile.get(fnRow).setValue(fsCol, foData);
                }else poMobile.get(fnRow).setValue(fsCol, null);
                break;
            default:
                poMobile.get(fnRow).setValue(fsCol, foData);
        }
    }
    
    public void setEmailInfo(int fnRow, int fnCol, Object foData){
        switch (fnCol) {
            case 2: //nEntryNox
                break;
            case 4: //nPriority
                if (foData instanceof Integer){
                    poEmail.get(fnRow).setValue(fnCol, foData);
                }else poEmail.get(fnRow).setValue(fnCol, 0);
                break;
            default:
                poEmail.get(fnRow).setValue(fnCol, foData);
        }
    }
    
    public void setEmailInfo(int fnRow, String fsCol, Object foData){
        switch (fsCol) {
            case "nEntryNox":
            case "nPriority":
                if (foData instanceof Integer){
                    poEmail.get(fnRow).setValue(fsCol, foData);
                }else poEmail.get(fnRow).setValue(fsCol, 0);
                break;
            default:
                poEmail.get(fnRow).setValue(fsCol, foData);
        }
    }
    
    public void setAddressInfo(int fnRow, int fnCol, Object foData){
        switch (fnCol) {
            case 2: //nEntryNox
            case 7: //nPriority
            case 8: //nLatitude
            case 9: //nLongitud
                if (foData instanceof Integer){
                    poAddress.get(fnRow).setValue(fnCol, foData);
                }else poAddress.get(fnRow).setValue(fnCol, 0);
                break;
            default:
                poAddress.get(fnRow).setValue(fnCol, foData);
        }
    }
    
    public void setAddressInfo(int fnRow, String fsCol, Object foData){
        switch (fsCol) {
            case "nEntryNox":
            case "nPriority":
                if (foData instanceof Integer){
                    poAddress.get(fnRow).setValue(fsCol, foData);
                }else poAddress.get(fnRow).setValue(fsCol, 0);
                break;
            case "nLatitude":
            case "nLongitud":
                if (foData instanceof Number){
                    poAddress.get(fnRow).setValue(fsCol, foData);
                }else poAddress.get(fnRow).setValue(fsCol, 0);
                break;
            default:
                poAddress.get(fnRow).setValue(fsCol, foData);
        }
    }
    
    public ArrayList<UnitClientMobile> getMobile(){return poMobile;}
    public ArrayList<UnitClientAddress> getAddress(){return poAddress;}
    public ArrayList<UnitClienteMail> getEmail(){return poEmail;}
    public void setMobile(ArrayList<UnitClientMobile> foMobile){this.poMobile = foMobile;}
    public void setAddress(ArrayList<UnitClientAddress> foAddress){this.poAddress = foAddress;}
    public void setEmail(ArrayList<UnitClienteMail> foEmail){this.poEmail = foEmail;}
    /*
    public void setMobile(int fnRow, int fnIndex, Object foValue){ poMobile.get(fnRow).setValue(fnIndex, foValue);}
    public void setMobile(int fnRow, String fsIndex, Object foValue){ poMobile.get(fnRow).setValue(fsIndex, foValue);}
    public Object getMobile(int fnRow, int fnIndex){return poMobile.get(fnRow).getValue(fnIndex);}
    public Object getMobile(int fnRow, String fsIndex){return poMobile.get(fnRow).getValue(fsIndex);}
    
    public void setEmail(int fnRow, int fnIndex, Object foValue){ poEmail.get(fnRow).setValue(fnIndex, foValue);}
    public void setEmail(int fnRow, String fsIndex, Object foValue){ poEmail.get(fnRow).setValue(fsIndex, foValue);}
    public Object getEmail(int fnRow, int fnIndex){return poEmail.get(fnRow).getValue(fnIndex);}
    public Object getEmail(int fnRow, String fsIndex){return poEmail.get(fnRow).getValue(fsIndex);}
    */
    private boolean saveMobile(String fsTransNox, boolean fbNewRecord) throws SQLException{
        if (MobileCount() <= 0){
            setMessage("No mobile number detected.");
            setErrMsg("Please encode client mobile number.");
            return false;
        }
        
        UnitClientMobile loDetail;
        UnitClientMobile loOldDet;
        int lnCtr;
        String lsSQL;
        
        for (lnCtr = 0; lnCtr <= MobileCount() -1; lnCtr++){
            poMobile.get(lnCtr).setClientID(fsTransNox);
            if (lnCtr == 0){
                if (poMobile.get(lnCtr).getClientID()== null || poMobile.get(lnCtr).getClientID().isEmpty()){
                    setMessage("Invalid client detected.");
                    return false;
                }
                
                if (poMobile.get(lnCtr).getMobileNo()== null || poMobile.get(lnCtr).getMobileNo().isEmpty()){
                    setMessage("Invalid mobile number detected.");
                    return false;
                }
            }else {
                if (poMobile.get(lnCtr).getClientID() == null || poMobile.get(lnCtr).getClientID().isEmpty()){ 
                    poMobile.remove(lnCtr);
                    return true;
                }
                
                if (poMobile.get(lnCtr).getMobileNo()== null || poMobile.get(lnCtr).getMobileNo().isEmpty()){ 
                    poMobile.remove(lnCtr);
                    return true;
                }
            }
            
            poMobile.get(lnCtr).setSubscriber(CommonUtils.classifyNetwork(poMobile.get(lnCtr).getMobileNo()));
            poMobile.get(lnCtr).setEntryNo(lnCtr + 1);
            poMobile.get(lnCtr).setDateModified(poGRider.getServerDate());
            
            if (fbNewRecord){
                //Generate the SQL Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poMobile.get(lnCtr));
            }else{
                //Load previous transaction
                loOldDet = loadClientMobile(fsTransNox, lnCtr + 1);
            
                //Generate the Update Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poMobile.get(lnCtr), (GEntity) loOldDet, 
                                            "sClientID = " + SQLUtil.toSQL(poMobile.get(lnCtr).getClientID()) + 
                                                " AND nEntryNox = " + poMobile.get(lnCtr).getEntryNo());
            }
            
            if (!lsSQL.equals("")){
                if(poGRider.executeQuery(lsSQL, pxeMobileTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()){ 
                        setErrMsg(poGRider.getErrMsg());
                        return false;
                    }
                }else {
                    setMessage("No record updated");
                }
            }
        }    
        
        //check if the new detail is less than the original detail count
        int lnRow = loadClientMobile(fsTransNox).size();
        if (lnCtr < lnRow -1){
            for (lnCtr = lnCtr + 1; lnCtr <= lnRow; lnCtr++){
                lsSQL = "DELETE FROM " + pxeMobileTable + 
                        " WHERE sClientID = " + SQLUtil.toSQL(fsTransNox) + 
                            " AND nEntryNox = " + lnCtr;
                
                if(poGRider.executeQuery(lsSQL, pxeMobileTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()) setErrMsg(poGRider.getErrMsg());
                }else {
                    setMessage("No record updated");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean saveEmail(String fsTransNox, boolean fbNewRecord) throws SQLException{
        if (eMailCount()<= 0){
            //not all customers have email
            return true;
        }
        
        UnitClienteMail loDetail;
        UnitClienteMail loOldDet;
        int lnCtr;
        String lsSQL;
        
        for (lnCtr = 0; lnCtr <= eMailCount() -1; lnCtr++){
            poEmail.get(lnCtr).setClientID(fsTransNox);
            if (lnCtr == 0){
                if (poEmail.get(lnCtr).getClientID()== null || poEmail.get(lnCtr).getClientID().isEmpty()){
                    setMessage("Invalid client detected.");
                    return false;
                }
                
                if (poEmail.get(lnCtr).getEMailAdd()== null || poEmail.get(lnCtr).getEMailAdd().isEmpty()){
                    setMessage("Invalid email address detected.");
                    return false;
                }
            }else {
                if (poEmail.get(lnCtr).getClientID() == null || poEmail.get(lnCtr).getClientID().isEmpty()){ 
                    poEmail.remove(lnCtr);
                    return true;
                }
                
                if (poEmail.get(lnCtr).getEMailAdd()== null || poEmail.get(lnCtr).getEMailAdd().isEmpty()){ 
                    poEmail.remove(lnCtr);
                    return true;
                }
            }
            
            poEmail.get(lnCtr).setEntryNo(lnCtr + 1);
            poEmail.get(lnCtr).setDateModified(poGRider.getServerDate());
            
            if (fbNewRecord){
                //Generate the SQL Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poEmail.get(lnCtr));
            }else{
                //Load previous transaction
                loOldDet = loadClientEmail(fsTransNox, lnCtr + 1);
            
                //Generate the Update Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poEmail.get(lnCtr), (GEntity) loOldDet, 
                                            "sClientID = " + SQLUtil.toSQL(poEmail.get(lnCtr).getClientID()) + 
                                                " AND nEntryNox = " + poEmail.get(lnCtr).getEntryNo());
            }
            
            if (!lsSQL.equals("")){
                if(poGRider.executeQuery(lsSQL, pxeEmailTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()){ 
                        setErrMsg(poGRider.getErrMsg());
                        return false;
                    }
                }else {
                    setMessage("No record updated");
                }
            }
        }    
        
        //check if the new detail is less than the original detail count
        int lnRow = loadClientMobile(fsTransNox).size();
        if (lnCtr < lnRow -1){
            for (lnCtr = lnCtr + 1; lnCtr <= lnRow; lnCtr++){
                lsSQL = "DELETE FROM " + pxeEmailTable + 
                        " WHERE sClientID = " + SQLUtil.toSQL(fsTransNox) + 
                            " AND nEntryNox = " + lnCtr;
                
                if(poGRider.executeQuery(lsSQL, pxeEmailTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()) setErrMsg(poGRider.getErrMsg());
                }else {
                    setMessage("No record updated");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean saveAddress(String fsTransNox, boolean fbNewRecord) throws SQLException{
        if (AddressCount()<= 0){
            setMessage("No address detected.");
            setErrMsg("Please encode client address.");
            return false;
        }
        
        UnitClientAddress loDetail;
        UnitClientAddress loOldDet;
        int lnCtr;
        String lsSQL;
        
        for (lnCtr = 0; lnCtr <= eMailCount() -1; lnCtr++){
            poAddress.get(lnCtr).setClientID(fsTransNox);
            if (lnCtr == 0){
                if (poAddress.get(lnCtr).getClientID() == null || poAddress.get(lnCtr).getClientID().isEmpty()){
                    setMessage("Invalid client detected. Please encode client address.");
                    return false;
                }
                
                if (poAddress.get(lnCtr).getAddress() == null || poAddress.get(lnCtr).getAddress().isEmpty()){
                    setMessage("Invalid address detected. Please encode client address.");
                    return false;
                }
                /*
                if (poAddress.get(lnCtr).getBrgyID()== null || poAddress.get(lnCtr).getBrgyID().isEmpty()){
                    setMessage("Invalid barangay detected.");
                    return false;
                }
                */
                if (poAddress.get(lnCtr).getTownID()== null || poAddress.get(lnCtr).getTownID().isEmpty()){
                    setMessage("Invalid town detected.");
                    return false;
                }
            }else {
                if (poAddress.get(lnCtr).getClientID() == null || poAddress.get(lnCtr).getClientID().isEmpty()){ 
                    poAddress.remove(lnCtr);
                    return true;
                }
                
                if (poAddress.get(lnCtr).getAddress()== null || poAddress.get(lnCtr).getAddress().isEmpty()){ 
                    poAddress.remove(lnCtr);
                    return true;
                }
                /*
                if (poAddress.get(lnCtr).getBrgyID()== null || poAddress.get(lnCtr).getBrgyID().isEmpty()){ 
                    poAddress.remove(lnCtr);
                    return true;
                }
                */
                if (poAddress.get(lnCtr).getTownID()== null || poAddress.get(lnCtr).getTownID().isEmpty()){ 
                    poAddress.remove(lnCtr);
                    return true;
                }
            }
            
            poAddress.get(lnCtr).setEntryNo(lnCtr + 1);
            poAddress.get(lnCtr).setDateModified(poGRider.getServerDate());
            
            if (fbNewRecord){
                //Generate the SQL Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poAddress.get(lnCtr));
            }else{
                //Load previous transaction
                loOldDet = loadClientAddress(fsTransNox, lnCtr + 1);
            
                //Generate the Update Statement
                lsSQL = MiscUtil.makeSQL((GEntity) poAddress.get(lnCtr), (GEntity) loOldDet, 
                                            "sClientID = " + SQLUtil.toSQL(poAddress.get(lnCtr).getClientID()) + 
                                                " AND nEntryNox = " + poAddress.get(lnCtr).getEntryNo());
            }
            
            if (!lsSQL.equals("")){
                if(poGRider.executeQuery(lsSQL, pxeAddressTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()){ 
                        setErrMsg(poGRider.getErrMsg());
                        return false;
                    }
                }else {
                    setMessage("No record updated");
                }
            }
        }    
        
        //check if the new detail is less than the original detail count
        int lnRow = loadClientMobile(fsTransNox).size();
        if (lnCtr < lnRow -1){
            for (lnCtr = lnCtr + 1; lnCtr <= lnRow; lnCtr++){
                lsSQL = "DELETE FROM " + pxeAddressTable + 
                        " WHERE sClientID = " + SQLUtil.toSQL(fsTransNox) + 
                            " AND nEntryNox = " + lnCtr;
                
                if(poGRider.executeQuery(lsSQL, pxeAddressTable, "", "") == 0){
                    if(!poGRider.getErrMsg().isEmpty()) setErrMsg(poGRider.getErrMsg());
                }else {
                    setMessage("No record updated");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private UnitClientMobile loadClientMobile(String fsTransNox, int fnEntryNox) throws SQLException{
        UnitClientMobile loObj = null;
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)) + 
                                                    " AND nEntryNox = " + fnEntryNox);
        
        if (!loRS.next()){
            setMessage("No Record Found");
        }else{
            //load each column to the entity
            loObj = new UnitClientMobile();
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loObj.setValue(lnCol, loRS.getObject(lnCol));
            }
        }      
        return loObj;
    }
    
    private ArrayList<UnitClientMobile> loadClientMobile(String fsTransNox) throws SQLException{
        UnitClientMobile loOcc = null;
        Connection loConn = null;
        loConn = setConnection();
        
        ArrayList<UnitClientMobile> loDetail = new ArrayList<>();
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)));
        
        if (MiscUtil.RecordCount(loRS) <= 0) return loDetail;
        
        loRS.first();
        while(loRS.next()){
            loOcc = new UnitClientMobile();
            //load each column to the entity
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loOcc.setValue(lnCol, loRS.getObject(lnCol));
            }

            loDetail.add(loOcc);
         }
        
        return loDetail;
    }
    
    private UnitClienteMail loadClientEmail(String fsTransNox, int fnEntryNox) throws SQLException{
        UnitClienteMail loObj = null;
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)) + 
                                                    " AND nEntryNox = " + fnEntryNox);
        
        if (!loRS.next()){
            setMessage("No Record Found");
        }else{
            //load each column to the entity
            loObj = new UnitClienteMail();
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loObj.setValue(lnCol, loRS.getObject(lnCol));
            }
        }      
        return loObj;
    }
    
    private ArrayList<UnitClienteMail> loadClientEmail(String fsTransNox) throws SQLException{
        UnitClienteMail loOcc = null;
        Connection loConn = null;
        loConn = setConnection();
        
        ArrayList<UnitClienteMail> loDetail = new ArrayList<>();
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)));
        
        if (MiscUtil.RecordCount(loRS) <= 0) return loDetail;
        
        loRS.first();
        while(loRS.next()){
            loOcc = new UnitClienteMail();
            //load each column to the entity
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loOcc.setValue(lnCol, loRS.getObject(lnCol));
            }

            loDetail.add(loOcc);
         }
        
        return loDetail;
    }
    
    private UnitClientAddress loadClientAddress(String fsTransNox, int fnEntryNox) throws SQLException{
        UnitClientAddress loObj = null;
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)) + 
                                                    " AND nEntryNox = " + fnEntryNox);
        
        if (!loRS.next()){
            setMessage("No Record Found");
        }else{
            //load each column to the entity
            loObj = new UnitClientAddress();
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loObj.setValue(lnCol, loRS.getObject(lnCol));
            }
        }      
        return loObj;
    }
    
    private ArrayList<UnitClientAddress> loadClientAddress(String fsTransNox) throws SQLException{
        UnitClientAddress loOcc = null;
        Connection loConn = null;
        loConn = setConnection();
        
        ArrayList<UnitClientAddress> loDetail = new ArrayList<>();
        ResultSet loRS = poGRider.executeQuery(
                            MiscUtil.addCondition(getSQ_Mobile(), 
                                                    "sClientID = " + SQLUtil.toSQL(fsTransNox)));
        
        if (MiscUtil.RecordCount(loRS) <= 0) return loDetail;
        
        loRS.first();
        while(loRS.next()){
            loOcc = new UnitClientAddress();
            //load each column to the entity
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loOcc.setValue(lnCol, loRS.getObject(lnCol));
            }

            loDetail.add(loOcc);
         }
        
        return loDetail;
    }
    
    private String getSQ_Mobile(){
        return "SELECT" +
                    "  sClientID" +
                    ", nEntryNox" +
                    ", sMobileNo" +
                    ", nPriority" +
                    ", cIncdMktg" +
                    ", nUnreachx" +
                    ", dLastVeri" +
                    ", dInactive" +
                    ", nNoRetryx" +
                    ", cInvalidx" +
                    ", cConfirmd" +
                    ", dConfirmd" +
                    ", cSubscrbr" +
                    ", dHoldMktg" +
                    ", dMktgMsg1" +
                    ", cNewMobil" +
                    ", dMktgMsg2" +
                    ", dMktgMsg3" +
                    ", cRecdStat" +
                    ", dModified" +
                " FROM " + pxeMobileTable +
                " ORDER BY nEntryNox";
    }
    
    private String getSQ_EMail(){
        return "SELECT" +
                    "  sClientID" +
                    ", nEntryNox" +
                    ", sEMailAdd" +
                    ", nPriority" +
                    ", cRecdStat" +
                    ", dModified" +
                " FROM " + pxeEmailTable +
                " ORDER BY nEntryNox";
    }
    
    private String getSQ_Address(){
        return "SELECT" +
                    "  sClientID" +
                    ", nEntryNox" +
                    ", sHouseNox" +
                    ", sAddressx" +
                    ", sBrgyIDxx" +
                    ", sTownIDxx" +
                    ", nPriority" +
                    ", nLatitude" +
                    ", nLongitud" +
                    ", cPrimaryx" +
                    ", cRecdStat" +
                    ", dModified" +
                " FROM " + pxeAddressTable +
                " ORDER BY nEntryNox";
    }
    
    //Added methods
    public void setGRider(GRider foGRider){
        this.poGRider = foGRider;
        this.psUserIDxx = foGRider.getUserID();
        
        if (psBranchCd.isEmpty()) psBranchCd = foGRider.getBranchCode();
        
        poMobile = new ArrayList<>();
    }
    
    public void setUserID(String fsUserID){
        this.psUserIDxx  = fsUserID;
    }
    
    private Connection setConnection(){
        Connection foConn;
        
        if (pbWithParent){
            foConn = (Connection) poGRider.getConnection();
            if (foConn == null) foConn = (Connection) poGRider.doConnect();
        }else foConn = (Connection) poGRider.doConnect();
        
        return foConn;
    }
    
    //Member Variables
    private GRider poGRider = null;
    private String psUserIDxx = "";
    private String psBranchCd = "";
    private String psWarnMsg = "";
    private String psErrMsgx = "";
    private boolean pbWithParent = false;
    private final GCrypt poCrypt = new GCrypt();
    
    private ArrayList<UnitClientMobile> poMobile;
    private ArrayList<UnitClienteMail> poEmail;
    private ArrayList<UnitClientAddress> poAddress;
    private final String pxeMobileTable = "Client_Mobile";
    private final String pxeEmailTable = "Client_eMail_Address";
    private final String pxeAddressTable = "Client_Address";
}