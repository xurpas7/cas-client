/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import org.rmj.cas.client.pojo.UnitClientAddress;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.client.base.XMClient;

public class ClientAddressNewController implements Initializable {

    @FXML
    private Button cmdClose;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField80;
    @FXML
    private AnchorPane anchor1;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private Button cmdOkay;
    @FXML
    private Button cmdCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initForm();
    }    

    @FXML
    private void cmdClose(ActionEvent event) {
        pbCancelled = true;
        unloadScene(event);
    }

    @FXML
    private void entryOK(ActionEvent event) {
        pbCancelled = false;
        unloadScene(event);
    }

    @FXML
    private void entryCancel(ActionEvent event) {
        pbCancelled = true;
        unloadScene(event);
    }
    
    private void unloadScene(ActionEvent event){
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private void initForm(){
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField01.setText(psClientID);
        txtField80.setText(psClientNm);
        txtField03.setText(poAddressArr.get(0).getHouseNo());
        txtField04.setText(poAddressArr.get(0).getAddress());
        
        psBrgyName = poAddressArr.get(0).getBrgyID();
        psTownName = poAddressArr.get(0).getTownID();
        
        String lsResult;
        String lsArrResult[];
        JSONObject jsonResult;
        
        if (!psBrgyName.equals("")){
            jsonResult = poClient.SearchBarangay(psBrgyName, true);
            
            if (jsonResult != null) psBrgyName = (String) jsonResult.get("sBrgyName");
            
            jsonResult.clear();
        }
        
        if (!psTownName.equals("")){
            jsonResult = poClient.SearchTown(psTownName, true);
            
            if (jsonResult != null) psTownName = (String) jsonResult.get("sTownName");
            
            jsonResult.clear();
        }
        
        txtField05.setText(psBrgyName);
        txtField06.setText(psTownName);
        pbLoaded = true;
    }
    
    public void setClientID(String fsClientID){this.psClientID = fsClientID;}
    public void setClientNm(String fsClientNm){this.psClientNm = fsClientNm;}
    
    public boolean isCancelled(){return pbCancelled;}
    
    public void setAddressList(ArrayList<UnitClientAddress> foAddresArr){this.poAddressArr = foAddresArr;}
    public ArrayList<UnitClientAddress> getAddressList(){return this.poAddressArr;}
    
    public void setAgent(XMClient foClient){this.poClient = foClient;}
    
    private XMClient poClient;
    private String psClientID;
    private String psClientNm;
    private String psTownName;
    private String psBrgyName;
    private boolean pbLoaded = false;
    private boolean pbCancelled = false;
    private ArrayList<UnitClientAddress> poAddressArr;
    ObservableList<Address> poAddressList = FXCollections.observableArrayList();
    
    private final String pxeModuleName = "ClientAddressNewController";
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 3:
                    if (lsValue.length() > 5) lsValue = lsValue.substring(0, 4);
                    
                    poAddressArr.get(0).setHouseNo(lsValue);
                    txtField.setText(poAddressArr.get(0).getHouseNo());
                    break;
                case 4:
                    if (lsValue.length() > 50) lsValue = lsValue.substring(0, 49);
                    
                    poAddressArr.get(0).setAddress(lsValue);
                    txtField.setText(poAddressArr.get(0).getAddress());
                    break;
                case 5:
                case 6:
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
        } else
            txtField.selectAll();
    };
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue;
        
        if (event.getCode() == F3 || event.getCode() == ENTER){
            if (txtField.getText() == null) lsValue = "";
            else lsValue = txtField.getText();
            
            JSONObject jsonResult;
            switch (lnIndex){
                case 5: //barangay
                    if (lsValue.equals(psBrgyName)){
                        if (event.getCode() == ENTER) CommonUtils.SetNextFocus(txtField);
                        return;
                    } 
                    
                    jsonResult = poClient.SearchBarangay(lsValue, false);
                    if (jsonResult != null) psBrgyName = (String) jsonResult.get("sBrgyName");
                    
                    break;
                case 6: //town
                    if (lsValue.equals(psTownName)){
                        if (event.getCode() == ENTER) CommonUtils.SetNextFocus(txtField);
                        return;
                    } 
                    
                    jsonResult = poClient.SearchTown(lsValue, false);
                    if (jsonResult != null) psTownName = (String) jsonResult.get("sTownName");
                    
                    break;
                default:
                    jsonResult = null;;
            }
            
            if (jsonResult != null){
                if (lnIndex == 5){
                    psBrgyName = (String) jsonResult.get("sBrgyName");
                    poAddressArr.get(0).setBrgyID((String) jsonResult.get("sBrgyIDxx"));
                    txtField.setText(psBrgyName);
                    
                    psTownName = (String) jsonResult.get("sTownName");
                    poAddressArr.get(0).setTownID((String) jsonResult.get("sTownIDxx"));
                    txtField06.setText(psTownName);
                }else{
                    psTownName = (String) jsonResult.get("sTownName");
                    poAddressArr.get(0).setTownID((String) jsonResult.get("sTownIDxx"));
                    txtField.setText(psTownName);
                }
                
            }
        }
        
        if (event.getCode() == ENTER){
            CommonUtils.SetNextFocus(txtField);
        } 
    }
}
