/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import org.rmj.cas.client.base.XMClient;
import org.rmj.cas.client.pojo.UnitClientAddress;
import org.rmj.cas.client.pojo.UnitClientMobile;
import org.rmj.cas.client.pojo.UnitClienteMail;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;

public class ClientMasterController implements Initializable{
    private final String pxeModuleName = "org.rmj.clientfx.application.ClientMasterContoller";
    
    private double xOffset = 0; 
    private double yOffset = 0;
    
    private String psCitizenx = "";
    private String psBirthPlc = "";
    private String psSpouseNm = "";
    
    ObservableList<String> cClientTp = FXCollections.observableArrayList("Company", "Individual");
    ObservableList<String> cGenderCd = FXCollections.observableArrayList("Male", "Female");
    ObservableList<String> cCvilStat = FXCollections.observableArrayList("Single", "Married", "Separated", "Widowed");

    @FXML private Button cmdAddress;
    @FXML private ComboBox cboClientTp;
    @FXML private ComboBox cboGender;
    @FXML private ComboBox cboCivilStat;
    @FXML private TextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private TextField txtField80;
    @FXML private TextField txtField10;
    @FXML private TextField txtField11;
    @FXML private TextField txtField12;
    @FXML private TextField txtField13;
    @FXML private TextField txtField81;
    @FXML private TextField txtField82;
    @FXML private Button cmdClose;
    @FXML private Button cmdCancel;
    @FXML private Button cmdOkay;
    @FXML private TextField txtField07;
    @FXML private TextField txtField14;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private FontAwesomeIconView glyphAddress;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poClient =  new XMClient(poGRider, poGRider.getBranchCode(), false);
        poClient.setOwner(poStage);
        initFields();
        initRecord();
    }

    @FXML
    private void modifyAddress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ClientFX.pxeClientAddressScreen));
        fxmlLoader.setLocation(getClass().getResource(ClientFX.pxeClientAddressScreen));
        
        //initialize mobile form
        ClientAddressNewController dialogClientAddress = new ClientAddressNewController();
        dialogClientAddress.setClientID((String) poClient.getMaster("sClientID"));
        dialogClientAddress.setClientNm((String) poClient.getMaster("sClientNm"));
        dialogClientAddress.setAddressList(poClient.getAddressList());
        dialogClientAddress.setAgent(poClient);
        
        fxmlLoader.setController(dialogClientAddress);
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        
        /*SET FORM MOVABLE*/
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        /*END SET FORM MOVABLE*/

        Scene scene = new Scene(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.showAndWait();
        
        if (dialogClientAddress.isCancelled() == false){
            poClient.setAddressList(dialogClientAddress.getAddressList());
            txtField80.setText(getAddressPriority(poClient.getAddressList()));
        }
    }

    @FXML
    private void closeForm(ActionEvent event) {
        psClientID = "";
        psClientNm = "";
        psCltAddrs = "";
        unloadScene(event);
    }

    @FXML
    private void entryCancel(ActionEvent event) {
        closeForm(event);
    }

    @FXML
    private void entryOK(ActionEvent event) {
        //validate other types of control
        poClient.setMaster(2, String.valueOf(cboClientTp.getSelectionModel().getSelectedIndex()));
        poClient.setMaster(8, String.valueOf(cboGender.getSelectionModel().getSelectedIndex()));
        poClient.setMaster(9, String.valueOf(cboCivilStat.getSelectionModel().getSelectedIndex()));        
        
        if (poClient.saveRecord()){
            psClientID = poClient.getMaster("sClientID").toString();
            psClientNm = poClient.getMaster("sClientNm").toString();
            //psCltAddrs = poClient.getMaster("sHouseNox").toString() + " " + 
            //                poClient.getMaster("sAddressx").toString();
            unloadScene(event);
        }// else closeForm(event);
    }
    
    private void unloadScene(ActionEvent event){
        Node source = (Node)  event.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private void unloadScene(MouseEvent event){
        Node source = (Node)  event.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue;
        
        if (txtField.getText() == null) lsValue = "";
            else lsValue = txtField.getText();
        
        if (event.getCode() == F3 || event.getCode() == ENTER){  
            switch (lnIndex){
                case 10:
                    try {
                        if (lsValue.equals(psCitizenx)){
                            if (event.getCode() == ENTER) CommonUtils.SetNextFocus(txtField);
                            return;
                        } else setMasterID(lnIndex, txtField);
                    } catch (SQLException e) {
                        ShowMessageFX.Error(poStage, e.getMessage(), pxeModuleName, "Please inform MIS Dept.");
                    } break;
                case 12:
                    try {
                        if (lsValue.equals(psBirthPlc)){
                            if (event.getCode() == ENTER) CommonUtils.SetNextFocus(txtField);
                            return;
                        } else setMasterID(lnIndex, txtField);
                    } catch (SQLException e) {
                        ShowMessageFX.Error(poStage, e.getMessage(), pxeModuleName, "Please inform MIS Dept.");
                    } break;
                case 14: 
                    try {
                        if (lsValue.equals(psSpouseNm)){
                            if (event.getCode() == ENTER) CommonUtils.SetNextFocus(txtField);
                            return;
                        } else setMasterID(lnIndex, txtField);
                    } catch (SQLException e) {
                        ShowMessageFX.Error(poStage, e.getMessage(), pxeModuleName, "Please inform MIS Dept.");
                    } break;
            }
        }
        
        if (event.getCode() == ENTER){
            CommonUtils.SetNextFocus(txtField);
        } 
    }
    
    private void setMasterID(int fnIndex, TextField foField) throws SQLException{
        String lsSQL = "";
        String lsCondition = "";
        JSONObject jsonResult;
        
        switch (fnIndex){
            case 10:
                jsonResult = poClient.SearchCountry(foField.getText(), false);
                
                if (jsonResult == null)
                    ShowMessageFX.Error(poStage, "No Record Found.", pxeModuleName, "No record found from the given criteria.");
                else{
                    psCitizenx = (String) jsonResult.get("sCntryNme");
                    foField.setText(psCitizenx);
                    poClient.setMaster(fnIndex, (String) jsonResult.get("sCntryCde"));
                }
                    
                break;
            case 12:
                jsonResult = poClient.SearchTown(foField.getText(), false);
                
                if (jsonResult == null)
                    ShowMessageFX.Error(poStage, "No Record Found.", pxeModuleName, "No record found from the given criteria.");
                else{
                    psBirthPlc = (String) jsonResult.get("sTownName");
                    foField.setText(psBirthPlc);
                    poClient.setMaster(fnIndex, (String) jsonResult.get("sTownIDxx"));
                }
                break;
            case 14:
                jsonResult = poClient.SearchClient(foField.getText(), false);
                
                if (jsonResult == null){
                    psSpouseNm = "";
                    foField.setText("");
                    poClient.setMaster(fnIndex, "");
                } else{
                    psSpouseNm = (String) jsonResult.get("sClientNm");
                    foField.setText(psSpouseNm);
                    poClient.setMaster(fnIndex, (String) jsonResult.get("sClientID"));
                }
        }
    }
    
    private String getMobilePriority(ArrayList<UnitClientMobile> foMobileArr){
        if(foMobileArr.isEmpty()) return "";
        
        String lsValue = "";
        int lnValue = foMobileArr.size();
        
        for (int lnCtr = 0; lnCtr <= foMobileArr.size()-1; lnCtr++){
            if (foMobileArr.get(lnCtr).getPriority() <= lnValue){
                lsValue = foMobileArr.get(lnCtr).getMobileNo();
                lnValue = foMobileArr.get(lnCtr).getPriority();
            }
        }
        return lsValue;
    }
    
    private String getEmailPriority(ArrayList<UnitClienteMail> foEmailArr){
        if(foEmailArr.isEmpty()) return "";
        
        String lsValue = "";
        int lnValue = foEmailArr.size();
        
        for (int lnCtr = 0; lnCtr <= foEmailArr.size()-1; lnCtr++){
            if (foEmailArr.get(lnCtr).getPriority() <= lnValue){
                lsValue = foEmailArr.get(lnCtr).getEMailAdd();
                lnValue = foEmailArr.get(lnCtr).getPriority();
            }
        }
        
        return lsValue;
    }
    
    private String getAddressPriority(ArrayList<UnitClientAddress> foAddressArr){
        if(foAddressArr.isEmpty()) return "";
        
        String lsValue = "";
        String lsAddress = "";
        String lsBrgyName = "";
        String lsTownName = "";
        JSONObject jsonResult;
        int lnValue = foAddressArr.size();
        
        for (int lnCtr = 0; lnCtr <= foAddressArr.size()-1; lnCtr++){
            if (foAddressArr.get(lnCtr).getPriority() <= lnValue){
                lsBrgyName = foAddressArr.get(lnCtr).getBrgyID();
                lsTownName = foAddressArr.get(lnCtr).getTownID();
                
                if (!lsBrgyName.equals("")){
                    jsonResult = poClient.SearchBarangay(lsBrgyName, true);
                    
                    if (jsonResult != null)
                        lsBrgyName = (String) jsonResult.get("sBrgyName");
                }
                
                if (!lsTownName.equals("")){
                    jsonResult = poClient.SearchBarangay(lsTownName, true);
                    
                    if (jsonResult != null)
                        lsTownName = (String) jsonResult.get("sTownName");
                }
                
                lsAddress = foAddressArr.get(lnCtr).getHouseNo() + " " + 
                            foAddressArr.get(lnCtr).getAddress();
                
                if (!lsBrgyName.equals("")) lsAddress = lsAddress + ", " + lsBrgyName + " " + lsTownName;
                else lsAddress = lsAddress + " " + lsTownName;
                
                lsValue = lsAddress;
                lnValue = foAddressArr.get(lnCtr).getPriority();
            }
        }
        
        return lsValue;
    }

    private void initMaster(){
        cboClientTp.setItems(cClientTp);
        cboClientTp.getSelectionModel().select(1);

        cboGender.setItems(cGenderCd);
        cboGender.getSelectionModel().select(0);

        cboCivilStat.setItems(cCvilStat);
        cboCivilStat.getSelectionModel().select(0);

        txtField01.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("");
        txtField80.setText("");
        txtField81.setText("");
        txtField82.setText("");
        
        cboClientTp.requestFocus();
    }

    private void initFields(){
        txtField03.focusedProperty().addListener(masterValidate);
        txtField04.focusedProperty().addListener(masterValidate);
        txtField05.focusedProperty().addListener(masterValidate);
        txtField06.focusedProperty().addListener(masterValidate);
        txtField07.focusedProperty().addListener(masterValidate);
        txtField10.focusedProperty().addListener(masterValidate);
        txtField11.focusedProperty().addListener(masterValidate);
        txtField12.focusedProperty().addListener(masterValidate);
        txtField13.focusedProperty().addListener(masterValidate);
        txtField14.focusedProperty().addListener(masterValidate);
        txtField81.focusedProperty().addListener(masterValidate);
        txtField82.focusedProperty().addListener(masterValidate);
        
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
        txtField81.setOnKeyPressed(this::txtField_KeyPressed);
        txtField82.setOnKeyPressed(this::txtField_KeyPressed);
        
        cboClientTp.setOnKeyPressed(this::Combo_KeyPressed);
        cboGender.setOnKeyPressed(this::Combo_KeyPressed);
        cboCivilStat.setOnKeyPressed(this::Combo_KeyPressed);
        
        txtField11.focusedProperty().addListener(masterFocus);
        txtField11.setPromptText(pxeDateFormat);
    }

    private void loadMaster(){
        poClient.setMaster("cClientTp", String.valueOf(pnClientTp));
        
        txtField01.setText((String) poClient.getMaster("sClientID"));
        txtField03.setText((String) poClient.getMaster("sLastName"));
        txtField04.setText((String) poClient.getMaster("sFrstName"));
        txtField05.setText((String) poClient.getMaster("sMiddName"));
        txtField06.setText((String) poClient.getMaster("sSuffixNm"));
        txtField07.setText((String) poClient.getMaster("sClientNm"));
        txtField10.setText((String) poClient.getMaster("sCitizenx"));
        txtField11.setText(CommonUtils.xsDateLong((Date)poClient.getMaster("dBirthDte")));
        txtField12.setText((String) poClient.getMaster("sBirthPlc"));
        txtField13.setText((String) poClient.getMaster("sAddlInfo"));
        txtField14.setText((String) poClient.getMaster("sSpouseID")); //load spouse name
        txtField80.setText("");
        txtField81.setText("");
        txtField82.setText("");
        
        cboClientTp.getSelectionModel().select(Integer.parseInt((String) poClient.getMaster("cClientTp")));
        cboGender.getSelectionModel().select(Integer.parseInt((String) poClient.getMaster("cGenderCd")));
        cboCivilStat.getSelectionModel().select(Integer.parseInt((String) poClient.getMaster("cCvilStat")));
        
        enableClientName();
    }
    
    private void enableClientName(){
        if (poClient.getMaster("cClientTp").equals("0")){
            txtField07.setEditable(true);
            txtField07.setFocusTraversable(true);
        }else{
            txtField07.setEditable(false);
            txtField07.setFocusTraversable(false);
        }
    }

    private void initRecord(){
        initMaster();

        pbNewRecord = psClientNm == null || psClientNm.isEmpty();

        if(pbNewRecord){
            if (poClient.newRecord()) loadMaster();
        }else{
        }
    }

    public String getClassName(){
        return this.getClass().getSimpleName();
    }

    public void setGRider(GRider foGrider){
        this.poGRider = foGrider;
    }
    
    public void setOwner(Stage foStage){
        this.poStage = foStage;
    }

    public void setClientName(String fsClientName){
        this.psClientNm = fsClientName;
    }
    
    public void setClientType(int fnValue){
        this.pnClientTp = fnValue;
    }
    
    public String getClientID(){return this.psClientID;}
    public String getClientNm(){return this.psClientNm;}
    public String getCltAddrs(){return this.psCltAddrs;}

    //Objects
    private GRider poGRider;
    private XMClient poClient;
    private Stage poStage;
    private int pnClientTp = 1;
    private String psClientID;
    private String psClientNm;
    private String psCltAddrs;
    private boolean pbNewRecord;
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = "1900-01-01";

    final ChangeListener<? super Boolean> masterFocus = (o,ov,nv)->{
        if(nv){
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue = txtField.getText();
            switch (lnIndex){
                case 11: /*dBirthDte*/
                    try {
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
                    } catch (ParseException e) {
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    
                    break;
                default:
            }
        }
    };
    final ChangeListener<? super Boolean> masterValidate = (o,ov,nv)->{
        if(!nv){
            TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
            int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            String lsValue;
            
            if (txtField.getText() == null) lsValue = "";
            else lsValue = txtField.getText();

            switch (lnIndex){
                case 3: /*sLastname*/
                case 4: /*sFrstName*/
                case 5: /*sMiddName*/
                    if (lsValue.length() > 60){
                        ShowMessageFX.Warning(poStage, "Please inform MIS Dept. if your entry was correct.", pxeModuleName, "Data too long");
                        txtField.requestFocus();
                    }
                    
                    poClient.setMaster(lnIndex, lsValue);
                    txtField.setText((String)poClient.getMaster(lnIndex));
                    txtField07.setText((String)poClient.getMaster("sClientNm"));
                    break;
                case 6: /*sSuffixNm*/
                    if (lsValue.length() > 8){
                        ShowMessageFX.Warning(poStage, "Please inform MIS Dept. if your entry was correct.", pxeModuleName, "Data too long");
                        txtField.requestFocus();
                    }
                    
                    poClient.setMaster(lnIndex, lsValue);
                    txtField.setText((String)poClient.getMaster(lnIndex));
                    txtField07.setText((String)poClient.getMaster("sClientNm"));
                    break;
                case 7: /*sClientNm*/
                    if (lsValue.length() > 256){
                        ShowMessageFX.Warning(poStage, "Please inform MIS Dept. if your entry was correct.", pxeModuleName, "Data too long");
                        txtField.requestFocus();
                    }
                    
                    poClient.setMaster(lnIndex, lsValue);
                    txtField.setText((String)poClient.getMaster(lnIndex));
                    break;
                case 13: /*sAddlInfo*/
                    if (lsValue.length() > 50) lsValue = lsValue.substring(0, 49);
                    
                    poClient.setMaster(lnIndex, lsValue); /*send the value to the class*/
                    txtField.setText((String)poClient.getMaster(lnIndex)); /*get the value from the class*/
                    break;
                case 10: //citizen id
                case 12: //birthplace id                   
                case 14: //spouse
                    break;
                case 11: //birthdate
                    if (CommonUtils.isDate(lsValue, pxeDateFormat)){
                        poClient.setMaster(lnIndex, CommonUtils.toDate(lsValue));
                    } else{
                        ShowMessageFX.Warning(poStage, "Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poClient.setMaster(lnIndex, CommonUtils.toDate(pxeDateDefault));
                    }
                    
                    txtField.setText(CommonUtils.xsDateLong((Date)poClient.getMaster(lnIndex))); /*get the value from the class*/
                    break;
                case 81: //mobile
                    if (lsValue.equals("")) break;
                    
                    if (lsValue.length() > 11){
                        ShowMessageFX.Warning(poStage, "Please verify your entry.", pxeModuleName, "Invalid mobile number.");  
                    } else if(!lsValue.substring(0, 1).equals("0")){
                        ShowMessageFX.Warning(poStage, "Please verify your entry.", pxeModuleName, "Invalid mobile number.");  
                    } else{
                        poClient.setMobile(0, "sMobileNo", lsValue);
                        poClient.setMobile(0, "nPriority", 1);
                        poClient.setMobile(0, "cIncdMktg", "1");
                    }
                    txtField.setText(poClient.getMobile(0, "sMobileNo").toString());
                    break;
                case 82: //email
                    if (lsValue.equals("")) break;
                    
                    if (!CommonUtils.isValidEmail(lsValue)){
                        ShowMessageFX.Warning(poStage, "Please verify your entry.", pxeModuleName, "Invalid email address.");  
                    } else {
                        if (lsValue.length() > 30){
                            ShowMessageFX.Warning(poStage, "Please inform MIS Dept. if your entry was correct.", pxeModuleName, "Email address too long.");  
                        } else{
                            poClient.setEmail(0, "sEMailAdd", lsValue);
                            poClient.setEmail(0, "nPriority", 1);
                        }
                    }
                    txtField.setText(poClient.getEmail(0, "sEMailAdd").toString());
                    break;
                default:
            }
        }
    };

    private void Combo_KeyPressed(KeyEvent event) {       
        if (event.getCode() == ENTER ||
            event.getCode() == KeyCode.TAB ||
            event.getCode() == KeyCode.DOWN){
            
            ComboBox combo = (ComboBox)event.getSource();
            
            switch (combo.getId()){
                case "cboClientTp":
                    poClient.setMaster(2, String.valueOf(cboClientTp.getSelectionModel().getSelectedIndex()));
                    enableClientName();
                    break;
                case "cboGender":
                    poClient.setMaster(8, String.valueOf(cboGender.getSelectionModel().getSelectedIndex()));
                    break;
                case "cboCivilStat":
                    poClient.setMaster(9, String.valueOf(cboCivilStat.getSelectionModel().getSelectedIndex()));      
            }
            CommonUtils.SetNextFocus(combo);
        } 
    }
}