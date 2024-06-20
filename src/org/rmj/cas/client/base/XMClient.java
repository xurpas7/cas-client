/**
 * @author  Michael Cuison
 * @date    2018-04-19
 */
package org.rmj.cas.client.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.iface.XMRecord;
import org.rmj.cas.client.pojo.UnitClientAddress;
import org.rmj.cas.client.pojo.UnitClientMaster;
import org.rmj.cas.client.pojo.UnitClientMobile;
import org.rmj.cas.client.pojo.UnitClienteMail;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.ui.showFXDialog;

public class XMClient implements XMRecord{
    public XMClient(GRider foGRider, String fsBranchCD, boolean fbWithParent){
        this.poGRider = foGRider;
        if (foGRider != null){
            this.pbWithParent = fbWithParent;
            this.psBranchCd = fsBranchCD;
            
            poControl = new Client();
            poControl.setGRider(foGRider);
            poControl.setBranch(fsBranchCD);
            poControl.setWithParent(fbWithParent);
            
            pnEditMode = EditMode.UNKNOWN;
        }
    }
    
    @Override
    public void setMaster(int fnCol, Object foData) {
        if (pnEditMode != EditMode.UNKNOWN){
            // Don't allow specific fields to assign values
            if(!(fnCol == poData.getColumn("sClientID") ||
                fnCol == poData.getColumn("cRecdStat") ||
                fnCol == poData.getColumn("sModified") ||
                fnCol == poData.getColumn("dModified"))){
                
                poData.setValue(fnCol, foData);
            }
        }
    }

    @Override
    public void setMaster(String fsCol, Object foData) {
        setMaster(poData.getColumn(fsCol), foData);
    }

    @Override
    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN || poControl == null)
            return null;
        else 
            return poData.getValue(fnCol);
        
    }

    @Override
    public Object getMaster(String fsCol) {
        return getMaster(poData.getColumn(fsCol));
    }

    @Override
    public boolean newRecord() {
        poData = poControl.newTransaction();              
        
        if (poData == null){
            return false;
        }else{
            addAddress();
            addMobile();
            addEmail();
            pnEditMode = EditMode.ADDNEW;
            return true;
        }
    }

    @Override
    public boolean openRecord(String fstransNox) {
        poData = poControl.loadTransaction(fstransNox);
        
        if (poData.getClientID()== null){
            ShowMessageFX.OkayCancel(poOwner, "Please inform MIS Dept.", pxeModulename, "Unable to load record.");
            return false;
        } else{
            pnEditMode = EditMode.READY;
            return true;
        }
    }

    @Override
    public boolean updateRecord() {
        if(pnEditMode != EditMode.READY) {
         return false;
      }
      else{
         pnEditMode = EditMode.UPDATE;
         return true;
      }
    }

    @Override
    public boolean saveRecord() {
        if(pnEditMode == EditMode.UNKNOWN){
            return false;
        }else{
            // Perform testing on values that needs approval here...
            
            poControl.setAddress(poAddressArr);
            poControl.setMobile(poMobileArr);
            poControl.setEmail(poEmailArr);
                
            UnitClientMaster loResult;
            if(pnEditMode == EditMode.ADDNEW)                        
                loResult = poControl.saveUpdate(poData, "");
            else loResult = poControl.saveUpdate(poData, (String) poData.getValue(1));

            if(loResult == null){
                if (!poControl.getErrMsg().isEmpty()){
                    ShowMessageFX.Error(poOwner, poControl.getErrMsg(), "Error", poControl.getMessage());
                }else{
                    ShowMessageFX.Warning(poOwner, poControl.getMessage(), "Warning", "");
                }
                
                return false;
            } else{
                pnEditMode = EditMode.READY;
                poData = loResult;
                
                //showMessage.Information(poControl.getMessage(), "Success", "");
                return true;
            }
      }
    }

    @Override
    public boolean deleteRecord(String fsTransNox) {
        if(pnEditMode != EditMode.READY){
            return false;
        } else{
            boolean lbResult = poControl.deleteTransaction(fsTransNox);
            if(lbResult) pnEditMode = EditMode.UNKNOWN;

            return lbResult;
        }
    }

    @Override
    public boolean deactivateRecord(String fsTransNox) {
        return false;
    }

    @Override
    public boolean activateRecord(String fsTransNox) {
        return false;
    }

    @Override
    public void setBranch(String foBranchCD) {
        psBranchCd = foBranchCD;
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }
    
    //Added methods
    public void setGRider(GRider foGrider){
        this.poGRider = foGrider;
        this.psUserIDxx = foGrider.getUserID();
        
        if (psBranchCd.isEmpty()) psBranchCd = poGRider.getBranchCode();
    }
    
    public void setOwner(Stage foStage){
        this.poOwner = foStage;
    }
    
    public boolean cancelRecord(String fsTransNox){
        if(pnEditMode != EditMode.READY){
            return false;
        } else{
            boolean lbResult = poControl.cancelTransaction(fsTransNox);
            if(lbResult) pnEditMode = EditMode.UNKNOWN;

            return lbResult;
        }
    }
    
    public boolean closeRecord(String fsTransNox){
        if(pnEditMode != EditMode.READY){
            return false;
        } else{
            boolean lbResult = poControl.closeTransaction(fsTransNox);
            if(lbResult) pnEditMode = EditMode.UNKNOWN;

            return lbResult;
        }
    }
    
    public boolean postRecord(String fsTransNox){
        if(pnEditMode != EditMode.READY){
            return false;
        } else{
            boolean lbResult = poControl.postTransaction(fsTransNox);
            if(lbResult) pnEditMode = EditMode.UNKNOWN;

            return lbResult;
        }
    }
    
    public boolean voidRecord(String fsTransNox){
        if(pnEditMode != EditMode.READY){
            return false;
        } else{
            boolean lbResult = poControl.voidTransaction(fsTransNox);
            if(lbResult) pnEditMode = EditMode.UNKNOWN;

            return lbResult;
        }
    }
    
    public boolean addAddress(){
        boolean lbAdd = poControl.addAddress();
        
        if (lbAdd){
            poControl.setAddressInfo(poControl.AddressCount() - 1, "nEntryNox", poControl.AddressCount());
            poControl.setAddressInfo(poControl.AddressCount() - 1, "nPriority", poControl.AddressCount());
            poAddressArr = poControl.getAddress();
            return true;
        } else return false;
    }
    public int getAddressCount(){return poControl.AddressCount();}
    
    public boolean addMobile(){ 
        boolean lbAdd =  poControl.addMobile();
        
        if (lbAdd){
            poControl.setMobileInfo(poControl.MobileCount() - 1, "nEntryNox", poControl.MobileCount());
            poControl.setMobileInfo(poControl.MobileCount() - 1, "nPriority", poControl.MobileCount());
            poMobileArr = poControl.getMobile();
            return true;
        }else return false;
    }
    public int getMobileCount(){return poControl.MobileCount();}
    
    public boolean addEmail(){
        boolean lbAdd = poControl.addEmail();
        
        if (lbAdd){
            poControl.setEmailInfo(poControl.eMailCount() - 1, "nEntryNox", poControl.eMailCount());
            poControl.setEmailInfo(poControl.eMailCount() - 1, "nPriority", poControl.eMailCount());
            poEmailArr = poControl.getEmail();
            return true;
            
        } else return false;
    }
    public int getEmailCount(){return poControl.eMailCount();}    
    public void setEmailInfo(int fnRow, int fnCol, Object foData) throws SQLException {
        if (pnEditMode != EditMode.UNKNOWN){
            // Don't allow specific fields to assign values
            if(!(fnCol == poEmail.getColumn("sClientID") ||
                fnCol == poEmail.getColumn("nEntryNox") ||
                fnCol == poEmail.getColumn("dModified"))){
                
                poControl.setEmailInfo(fnRow, fnCol, foData);
            }
        }
    }
    
    public JSONObject SearchBarangay(String fsValue, boolean fbByCode){
        String lsSQL = "SELECT " +
                            "  a.sBrgyIDxx" +
                            ", a.sBrgyName" +
                            ", b.sTownName" + 
                            ", b.sZippCode" +
                            ", c.sProvName" + 
                            ", c.sProvIDxx" +
                            ", b.sTownIDxx" +
                        " FROM Barangay a" + 
                            ", TownCity b" +
                            ", Province c" +
                        " WHERE a.sTownIDxx = b.sTownIDxx" + 
                            " AND b.sProvIDxx = c.sProvIDxx" + 
                            " AND a.cRecdStat = '1'" + 
                            " AND b.cRecdStat = '1'" + 
                            " AND c.cRecdStat = '1'";
        
        if (fbByCode){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrgyIDxx = " + SQLUtil.toSQL(fsValue));
            
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            
            return showFXDialog.jsonBrowse(poGRider, 
                                            loRS, 
                                            "ID»Barangay»Town»Province", 
                                            "sBrgyIDxx»sBrgyName»sTownName»sProvName");
        }
        
        return showFXDialog.jsonSearch(poGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        "ID»Barangay»Town»Province", 
                                        "sBrgyIDxx»sBrgyName»sTownName»sProvName", 
                                        "a.sBrgyIDxx»a.sBrgyName»b.sTownName»c.sProvName", 
                                        1);
    }
    
    public JSONObject SearchTown(String fsValue, boolean fbByCode){
        String lsSQL = "SELECT " +
                            "  a.sTownIDxx" +
                            ", a.sTownName" + 
                            ", a.sZippCode" +
                            ", b.sProvName" + 
                            ", b.sProvIDxx" +
                        " FROM TownCity a" +
                            ", Province b" +
                        " WHERE a.sProvIDxx = b.sProvIDxx" + 
                            " AND a.cRecdStat = '1'";
        
        if (fbByCode){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sTownIDxx = " + SQLUtil.toSQL(fsValue));
            
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            
            return showFXDialog.jsonBrowse(poGRider, 
                                            loRS, 
                                            "ID»Town»Postal Code»Province", 
                                            "sTownIDxx»sTownName»sZippCode»sProvName");
        }
        
        return showFXDialog.jsonSearch(poGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        "ID»Town»Postal Code»Province", 
                                        "sTownIDxx»sTownName»sZippCode»sProvName", 
                                        "a.sTownIDxx»a.sTownName»a.sZippCode»b.sProvName", 
                                        1);
    }
    
    public JSONObject SearchCountry(String fsValue, boolean fbByCode){
        String lsSQL = "SELECT" +
                            "  sCntryCde" +
                            ", sCntryNme" +
                        " FROM Country" +
                        " WHERE cRecdStat = '1'";
        
        if (fbByCode){
            lsSQL = MiscUtil.addCondition(lsSQL, "sCntryCde = " + SQLUtil.toSQL(fsValue));
            
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            
            return showFXDialog.jsonBrowse(poGRider, 
                                            loRS, 
                                            "Code»Name", 
                                            "sCntryCde»sCntryNme");
        }
        
        return showFXDialog.jsonSearch(poGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        "Code»Name", 
                                        "sCntryCde»sCntryNme", 
                                        "sCntryCde»sCntryNme", 
                                        1);
    }
    
    public JSONObject SearchClient(String fsValue, boolean fbByCode){
        String lsHeader = "ID»Name»Address»Last Name»First Name»Midd Name»Suffix";
        String lsColName = "sClientID»sClientNm»xAddressx»sLastName»sFrstName»sMiddName»sSuffixNm";
        String lsColCrit = "a.sClientID»a.sClientNm»CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName)»a.sLastName»a.sFrstName»a.sMiddName»a.sSuffixNm";
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", a.sClientNm" +
                            ", CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName) xAddressx" +
                            ", a.sLastName" + 
                            ", a.sFrstName" + 
                            ", a.sMiddName" + 
                            ", a.sSuffixNm" + 
                        " FROM Client_Master a" + 
                            " LEFT JOIN Client_Address b" + 
                                " ON a.sClientID = b.sClientID" + 
                                    " AND b.nPriority = 1" +
                            " LEFT JOIN TownCity c" + 
                                " ON b.sTownIDxx = c.sTownIDxx" + 
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx";
        
        return showFXDialog.jsonSearch(poGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fbByCode ? 0 :1);
    }
    
    public JSONObject BrowseClient(String fsValue, boolean fbByCode){
        String lsHeader = "ID»Name»Address»Last Name»First Name»Midd Name»Suffix";
        String lsColName = "sClientID»sClientNm»xAddressx»sLastName»sFrstName»sMiddName»sSuffixNm";        
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", a.sClientNm" +
                            ", CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName) xAddressx" +
                            ", a.sLastName" + 
                            ", a.sFrstName" + 
                            ", a.sMiddName" + 
                            ", a.sSuffixNm" + 
                        " FROM Client_Master a" + 
                            " LEFT JOIN Client_Address b" + 
                                " ON a.sClientID = b.sClientID" + 
                                    " AND b.nPriority = 1" +
                            " LEFT JOIN TownCity c" + 
                                " ON b.sTownIDxx = c.sTownIDxx" + 
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx";
        if (fbByCode)
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sClientID = " + SQLUtil.toSQL(fsValue));
        else 
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sClientNm LIKE " + SQLUtil.toSQL(fsValue + "%"));
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        return showFXDialog.jsonBrowse(poGRider, loRS, lsHeader, lsColName);
    }
    
    public ArrayList<UnitClientMobile> getMobileList(){return poMobileArr;}
    public void setMobileList(ArrayList<UnitClientMobile> foObj){this.poMobileArr = foObj;}
    
    public ArrayList<UnitClientAddress> getAddressList(){return poAddressArr;}
    public void setAddressList(ArrayList<UnitClientAddress> foObj){this.poAddressArr = foObj;}
    
    public ArrayList<UnitClienteMail> getEmailList(){return poEmailArr;}
    public void setEmailList(ArrayList<UnitClienteMail> foObj){this.poEmailArr = foObj;}
    
    public void setMobile(int fnRow, int fnIndex, Object foValue){ poMobileArr.get(fnRow).setValue(fnIndex, foValue);}
    public void setMobile(int fnRow, String fsIndex, Object foValue){ poMobileArr.get(fnRow).setValue(fsIndex, foValue);}
    public Object getMobile(int fnRow, int fnIndex){return poMobileArr.get(fnRow).getValue(fnIndex);}
    public Object getMobile(int fnRow, String fsIndex){return poMobileArr.get(fnRow).getValue(fsIndex);}
    
    public void setEmail(int fnRow, int fnIndex, Object foValue){ poEmailArr.get(fnRow).setValue(fnIndex, foValue);}
    public void setEmail(int fnRow, String fsIndex, Object foValue){ poEmailArr.get(fnRow).setValue(fsIndex, foValue);}
    public Object getEmail(int fnRow, int fnIndex){return poEmailArr.get(fnRow).getValue(fnIndex);}
    public Object getEmail(int fnRow, String fsIndex){return poEmailArr.get(fnRow).getValue(fsIndex);}
    
    //Member Variables
    private GRider poGRider;
    private Client poControl;
    private UnitClientMaster poData;
    private Stage poOwner;
    
    private final UnitClientAddress poAddress = new UnitClientAddress();
    private ArrayList<UnitClientAddress> poAddressArr;
    
    private final UnitClientMobile poMobile = new UnitClientMobile();
    private ArrayList<UnitClientMobile> poMobileArr;
    
    private final UnitClienteMail poEmail = new UnitClienteMail();
    private ArrayList<UnitClienteMail> poEmailArr;
    
    private String psBranchCd;
    private int pnEditMode;
    private String psUserIDxx;
    private boolean pbWithParent;
    
    private final String pxeModulename = "XMClient";
}
