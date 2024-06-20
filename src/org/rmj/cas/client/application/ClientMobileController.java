/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import org.rmj.cas.client.pojo.UnitClientMobile;
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

/**
 * FXML Controller class
 *
 * @author micha
 */
public class ClientMobileController implements Initializable{
    @FXML
    private Button cmdClose;
    @FXML
    private Button cmdSave; 
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField80;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField02;
    @FXML
    private TableView gridDetail;
    @FXML
    private CheckBox chkIncdMktg;
    @FXML
    private CheckBox chkRecdStat;
    @FXML
    private AnchorPane anchor1;
    @FXML
    private Button cmdModify;
    @FXML
    private Button cmdUp;
    @FXML
    private Button cmdDown;
    @FXML
    private Button cmdCancel;

    @FXML
    private void closeForm(ActionEvent event) {
        pbCancelled = true;
        unloadScene(event);
    }

    @FXML
    private void saveMobile(ActionEvent event) {
        pbCancelled = false;
        unloadScene(event);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initForm();
        initGrid();
        loadDetail();
    }
    
    @FXML
    private void gridDetail_Click(MouseEvent event) {
        int lnRow = gridDetail.getSelectionModel().getSelectedIndex();
        
        txtField02.setText(poMobileArr.get(lnRow).getMobileNo());
        txtField03.setText(String.valueOf(poMobileArr.get(lnRow).getPriority()));
        
        chkIncdMktg.setSelected(("1".equals((String)poMobileArr.get(lnRow).getIncldMktg())));
        chkRecdStat.setSelected(("1".equals((String)poMobileArr.get(lnRow).getRecordStat())));
        
        pnIndex = lnRow;
    }

    @FXML
    private void cancelUpdate(ActionEvent event) {
        txtField02.setText(""); 
        txtField03.setText("");
        chkIncdMktg.setSelected(false); 
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdModify.setText("Update");
        cmdCancel.setVisible(false);
    }
    
    @FXML
    private void updateDetail(ActionEvent event) {
        if (!txtField03.getText().equals("")){
            if (cmdModify.getText().equals("Update")){
                anchor1.setDisable(false);
                cmdModify.setText("Commit");
                cmdCancel.setVisible(true);

                if (txtField02.getText().equals("")){
                    txtField02.setEditable(true);
                    txtField02.requestFocus();
                }else{
                    txtField02.setEditable(false);
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
    
    private void unloadScene(ActionEvent event){
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    public void setClientID(String fsClientID){this.psClientID = fsClientID;}
    public void setClientNm(String fsClientNm){this.psClientNm = fsClientNm;}
    
    public boolean isCancelled(){return pbCancelled;}
    
    public void setMobileList(ArrayList<UnitClientMobile> foMobileArr){this.poMobileArr = foMobileArr;}
    public ArrayList<UnitClientMobile> getMobileList(){return this.poMobileArr;}
    
    private void initForm(){
        txtField01.setText(psClientID);
        txtField80.setText(psClientNm);
        txtField02.setText("");
        txtField03.setText("");
        
        chkIncdMktg.setSelected(false);
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdCancel.setVisible(false);
        cmdModify.setText("Update");
    }
    
    private void initGrid(){
        /*Header Title*/
        TableColumn primary = new TableColumn("MOBILE NUMBERS");
        TableColumn entryno = new TableColumn("NO.");
        TableColumn mobile = new TableColumn("MOBILE NO.");
        TableColumn priority = new TableColumn("PRIORITY");   
        
        /*Set size of the cells*/
        entryno.setMinWidth(50); mobile.setMaxWidth(50);
        mobile.setMinWidth(250); mobile.setMaxWidth(250);
        priority.setMinWidth(94); mobile.setMaxWidth(94);
        
        /*Set columns non-sortable*/
        entryno.setSortable(false);
        mobile.setSortable(false);
        priority.setSortable(false);
        
        /*Add the header*/
        primary.getColumns().addAll(entryno, mobile, priority);
        gridDetail.getColumns().clear();
        gridDetail.getColumns().addAll(primary);
        
        /*Bind the data source to the cells*/
        entryno.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("entryno"));      
        mobile.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("mobile"));
        priority.setCellValueFactory(new PropertyValueFactory<MobileNumber,String>("priority"));
        
        /*Set data to table*/
        gridDetail.setItems(poMobileList);
    }
    
    private void loadDetail(){
        /*Set the value of ArrayList(poMobileArr) to Observable List for table use.*/
        int lnCtr;
        int lnRow = poMobileArr.size();
        
        poMobileList.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            poMobileList.add(new MobileNumber(String.valueOf(poMobileArr.get(lnCtr).getEntryNo()), 
                                                poMobileArr.get(lnCtr).getMobileNo(), 
                                                String.valueOf(poMobileArr.get(lnCtr).getPriority())));
        }
        
        /*FOCUS ON FIRST ROW*/
        if (!poMobileList.isEmpty()){
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
        poMobileArr.get(pnIndex).setMobileNo(txtField02.getText());
        poMobileArr.get(pnIndex).setIncldMktg(chkIncdMktg.isSelected() ? "1" : "0");
        poMobileArr.get(pnIndex).setRecordStat(chkRecdStat.isSelected() ? "1" : "0");
        
        loadDetail();
    }
    
    private String psClientID;
    private String psClientNm;
    private int pnIndex = 0;
    private boolean pbCancelled = false;
    private ArrayList<UnitClientMobile> poMobileArr;
    ObservableList<MobileNumber> poMobileList = FXCollections.observableArrayList();
}
