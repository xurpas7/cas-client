/**
 * @author Michael Cuison
 * @since 2018
*/

package org.rmj.cas.client.application;

import org.rmj.cas.client.pojo.UnitClienteMail;
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
public class ClientEmailController implements Initializable {

    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField80;
    @FXML
    private AnchorPane anchor1;
    @FXML
    private TextField txtField03;
    @FXML
    private Button cmdUp;
    @FXML
    private Button cmdDown;
    @FXML
    private CheckBox chkRecdStat;
    @FXML
    private TextField txtField02;
    @FXML
    private TableView gridDetail;
    @FXML
    private Button cmdModify;
    @FXML
    private Button cmdCancel;
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
    private void gridDetail_Click(MouseEvent event) {
        int lnRow = gridDetail.getSelectionModel().getSelectedIndex();
        
        txtField02.setText(poEmailArr.get(lnRow).getEMailAdd());
        txtField03.setText(String.valueOf(poEmailArr.get(lnRow).getPriority()));
        
        chkRecdStat.setSelected(("1".equals((String)poEmailArr.get(lnRow).getRecordStat())));
        
        pnIndex = lnRow;
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

    @FXML
    private void cancelUpdate(ActionEvent event) {
        txtField02.setText(""); 
        txtField03.setText("");
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdModify.setText("Update");
        cmdCancel.setVisible(false);
    }

    @FXML
    private void saveEmail(ActionEvent event) {
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
    
    public void setClientID(String fsClientID){this.psClientID = fsClientID;}
    public void setClientNm(String fsClientNm){this.psClientNm = fsClientNm;}
    
    public boolean isCancelled(){return pbCancelled;}
    
    public void setEmailList(ArrayList<UnitClienteMail> foEmailArr){this.poEmailArr = foEmailArr;}
    public ArrayList<UnitClienteMail> getEmailList(){return this.poEmailArr;}
    
    private void initForm(){
        txtField01.setText(psClientID);
        txtField80.setText(psClientNm);
        txtField02.setText("");
        txtField03.setText("");
        
        chkRecdStat.setSelected(false);
        
        anchor1.setDisable(true);
        cmdCancel.setVisible(false);
        cmdModify.setText("Update");
    }
    
    private void initGrid(){
        /*Header Title*/
        TableColumn primary = new TableColumn("EMAIL ADDRESS");
        TableColumn entryno = new TableColumn("NO.");
        TableColumn email = new TableColumn("EMAIL ADD");
        TableColumn priority = new TableColumn("PRIORITY");   
        
        /*Set size of the cells*/
        entryno.setMinWidth(50); entryno.setMaxWidth(50);
        email.setMinWidth(250); email.setMaxWidth(250);
        priority.setMinWidth(94); priority.setMaxWidth(94);
        
        /*Set columns non-sortable*/
        entryno.setSortable(false);
        email.setSortable(false);
        priority.setSortable(false);
        
        /*Add the header*/
        primary.getColumns().addAll(entryno, email, priority);
        gridDetail.getColumns().clear();
        gridDetail.getColumns().addAll(primary);
        
        /*Bind the data source to the cells*/
        entryno.setCellValueFactory(new PropertyValueFactory<EmailAddress,String>("entryno"));      
        email.setCellValueFactory(new PropertyValueFactory<EmailAddress,String>("email"));
        priority.setCellValueFactory(new PropertyValueFactory<EmailAddress,String>("priority"));
        
        /*Set data to table*/
        gridDetail.setItems(poEmailList);
    }
    
    private void loadDetail(){
        /*Set the value of ArrayList(poEmailArr) to Observable List for table use.*/
        int lnCtr;
        int lnRow = poEmailArr.size();
        
        poEmailList.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            poEmailList.add(new EmailAddress(String.valueOf(poEmailArr.get(lnCtr).getEntryNo()), 
                                                poEmailArr.get(lnCtr).getEMailAdd(), 
                                                String.valueOf(poEmailArr.get(lnCtr).getPriority())));
        }
        
        /*FOCUS ON FIRST ROW*/
        if (!poEmailList.isEmpty()){
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
        poEmailArr.get(pnIndex).setEMailAdd(txtField02.getText());
        poEmailArr.get(pnIndex).setRecordStat(chkRecdStat.isSelected() ? "1" : "0");
        
        loadDetail();
    }
    
    private String psClientID;
    private String psClientNm;
    private int pnIndex = 0;
    private boolean pbCancelled = false;
    private ArrayList<UnitClienteMail> poEmailArr;
    ObservableList<EmailAddress> poEmailList = FXCollections.observableArrayList();
}
