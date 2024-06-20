/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;;

import org.rmj.cas.client.pojo.UnitClientAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmj.appdriver.agentfx.ShowMessageFX;

public class ClientAddressController implements Initializable {

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
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private CheckBox chkPrimary;
    @FXML
    private CheckBox chkRecdStat;
    @FXML
    private TextField txtField07;
    @FXML
    private Button cmdUp;
    @FXML
    private Button cmdDown;
    @FXML
    private Button cmdModify;
    @FXML
    private Button cmdCancel;
    @FXML
    private TableView gridDetail;
    @FXML
    private Button cmdSave;
    @FXML
    private Button cmdClose;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initForm();
        initGrid();
        loadDetail();
    }    

    @FXML
    private void updateDetail(ActionEvent event) {
        if (!txtField07.getText().equals("")){
            if (cmdModify.getText().equals("Update")){
                anchor1.setDisable(false);
                cmdModify.setText("Commit");
                cmdCancel.setVisible(true);

                if (txtField03.getText().equals("") ||
                    txtField04.getText().equals("") ||
                    txtField05.getText().equals("") ||
                    txtField06.getText().equals("")){
                    
                    txtField03.setEditable(txtField03.getText().equals(""));
                    txtField04.setEditable(txtField04.getText().equals(""));
                    txtField05.setEditable(txtField05.getText().equals(""));
                    txtField06.setEditable(txtField06.getText().equals(""));
                    //txtField08.setEditable(txtField08.getText().equals(""));
                    //txtField09.setEditable(txtField09.getText().equals(""));
                    
                    txtField03.requestFocus();
                }else{
                    txtField03.setEditable(false);
                    txtField04.setEditable(false);
                    txtField05.setEditable(false);
                    txtField06.setEditable(false);
                    //txtField08.setEditable(false);
                    //txtField09.setEditable(false);
                    
                    cmdUp.requestFocus();
                }
            }else{
                anchor1.setDisable(true);
                cmdModify.setText("Update");
                
                submitDetail();
            }
        } else{
            ShowMessageFX.Information("Please select an item on the list.", "Notice", "No Item Selected");
        }
    }

    @FXML
    private void cancelUpdate(ActionEvent event) {
        txtField03.setText(""); 
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdModify.setText("Update");
        cmdCancel.setVisible(false);
    }

    @FXML
    private void gridDetail_Click(MouseEvent event) {
        int lnRow = gridDetail.getSelectionModel().getSelectedIndex();
        
        txtField03.setText(poAddressArr.get(lnRow).getHouseNo());
        txtField04.setText(poAddressArr.get(lnRow).getAddress());
        txtField05.setText(poAddressArr.get(lnRow).getBrgyID());
        txtField06.setText(poAddressArr.get(lnRow).getTownID());
        txtField07.setText(String.valueOf(poAddressArr.get(lnRow).getEntryNo()));
        
        if (poAddressArr.get(lnRow).getLatitude() != null){
            txtField08.setText(String.valueOf(poAddressArr.get(lnRow).getLatitude()));
        } else{txtField08.setText("");}
        
        if (poAddressArr.get(lnRow).getLongitude() != null){
            txtField09.setText(String.valueOf(poAddressArr.get(lnRow).getLongitude()));
        } else{txtField09.setText("");}
        
        
        chkRecdStat.setSelected(("1".equals((String)poAddressArr.get(lnRow).getRecordStat())));
        
        pnIndex = lnRow;
    }

    @FXML
    private void saveAddress(ActionEvent event) {
        pbCancelled = false;
        unloadScene(event);
    }

    @FXML
    private void closeForm(ActionEvent event) {
        pbCancelled = true;
        unloadScene(event);
    }
    
    private void unloadScene(ActionEvent event){
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private void initForm(){
        txtField01.setText(psClientID);
        txtField80.setText(psClientNm);
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdCancel.setVisible(false);
        cmdModify.setText("Update");
    }
    
    private void initGrid(){
        /*Header Title*/
        TableColumn primary = new TableColumn("CLIENT ADDRESS");
        TableColumn entryno = new TableColumn("NO.");
        TableColumn address = new TableColumn("LOCATION");
        TableColumn priority = new TableColumn("PRIORITY");   
        
        /*Set size of the cells*/
        entryno.setMinWidth(50); entryno.setMaxWidth(50);
        address.setMinWidth(250); address.setMaxWidth(250);
        priority.setMinWidth(94); priority.setMaxWidth(94);
        
        /*Set columns non-sortable*/
        entryno.setSortable(false);
        address.setSortable(false);
        priority.setSortable(false);
        
        /*Add the header*/
        primary.getColumns().addAll(entryno, address, priority);
        gridDetail.getColumns().clear();
        gridDetail.getColumns().addAll(primary);
        
        /*Bind the data source to the cells*/
        entryno.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("entryno"));      
        address.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("address"));
        priority.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("priority"));
        
        /*Set data to table*/
        gridDetail.setItems(poAddressList);
    }
    
    private void loadDetail(){
        /*Set the value of ArrayList(poMobileArr) to Observable List for table use.*/
        int lnCtr;
        int lnRow = poAddressArr.size();
        
        poAddressList.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            String lsAddress = poAddressArr.get(lnCtr).getHouseNo() + " " + 
                                poAddressArr.get(lnCtr).getAddress();
                                /**
                                 * add barangay, town, postal code, province
                                 */
            
            poAddressList.add(new Address(String.valueOf(poAddressArr.get(lnCtr).getEntryNo()), 
                                            lsAddress, 
                                            String.valueOf(poAddressArr.get(lnCtr).getPriority())));
        }
        
        /*FOCUS ON FIRST ROW*/
        if (!poAddressList.isEmpty()){
            gridDetail.requestFocus();
            gridDetail.getSelectionModel().select(0);
            gridDetail.getFocusModel().focus(0);
        }
    }
    
    private void setPriority(boolean fbMoveUp){
        if (fbMoveUp){
        }else{}
    }
    
    private void submitDetail(){
        poAddressArr.get(pnIndex).setHouseNo(txtField03.getText());
        poAddressArr.get(pnIndex).setAddress(txtField04.getText());
        poAddressArr.get(pnIndex).setBrgyID(txtField05.getText());
        poAddressArr.get(pnIndex).setTownID(txtField06.getText());
        
        if (!txtField08.getText().equals("")) {poAddressArr.get(pnIndex).setLatitude(Double.parseDouble(txtField08.getText()));}
        if (!txtField09.getText().equals("")) {poAddressArr.get(pnIndex).setLongitude(Double.parseDouble(txtField09.getText()));}
        
        poAddressArr.get(pnIndex).setIsPrimary(chkPrimary.isSelected() ? "1" : "0");
        poAddressArr.get(pnIndex).setRecordStat(chkRecdStat.isSelected() ? "1" : "0");
        
        loadDetail();
    }
    
    public void setClientID(String fsClientID){this.psClientID = fsClientID;}
    public void setClientNm(String fsClientNm){this.psClientNm = fsClientNm;}
    
    public boolean isCancelled(){return pbCancelled;}
    
    public void setAddressList(ArrayList<UnitClientAddress> foMobileArr){this.poAddressArr = foMobileArr;}
    public ArrayList<UnitClientAddress> getAddressList(){return this.poAddressArr;}
    
    private String psClientID;
    private String psClientNm;
    private int pnIndex = 0;
    private boolean pbCancelled = false;
    private ArrayList<UnitClientAddress> poAddressArr;
    ObservableList<Address> poAddressList = FXCollections.observableArrayList();
}
