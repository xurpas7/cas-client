package org.rmj.cas.client.application;

import org.rmj.cas.client.base.XMClient;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;

public class ClientFX extends Application {
    public static String pxeClientMaster = "ClientMaster";
    public static String pxeClientMasterScreen = "ClientMaster.fxml";
    public static String pxeClientMobile = "ClientMobile";
    public static String pxeClientMobileScreen = "ClientMobile.fxml";
    public static String pxeClientAddress = "ClientAddress";
    public static String pxeClientAddressScreen = "ClientAddressNew.fxml";
    public static String pxeClientEmail = "ClientEmail";
    public static String pxeClientEmailScreen = "ClientEmail.fxml";
    
    private double xOffset = 0; 
    private double yOffset = 0;
    
    public static GRider poGRider;
    public static XMClient poClient;
    public static int pnClientTp = 1;
    
    private String psClientID = "";
    private String psClientNm = "";
    private String psCltAddrs = "";
    
    public String getClassName(){return this.getClass().getSimpleName();}
    public String getClientID(){return this.psClientID;}
    public String getClientNm(){return this.psClientNm;}
    public String getCltAddrs(){return this.psCltAddrs;}
    public void setGRider(GRider foGRider){poGRider = foGRider;}
            
    @Override
    public void start(Stage primaryStage) throws Exception {   
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(ClientFX.pxeClientMasterScreen));
        
        //get controller
        ClientMasterController oClient = new ClientMasterController();
        //assign objects to controller
        oClient.setGRider(poGRider);
        oClient.setClientType(pnClientTp);
        oClient.setOwner(primaryStage);
        
        fxmlLoader.setController(oClient);
        Parent parent = fxmlLoader.load();
        
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
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        /*END - SET FORM MOVABLE*/

        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Add new client");
        primaryStage.getIcons().add(new Image("org/rmj/appdriver/agentfx/styles/64.png"));
        primaryStage.showAndWait();
        
        if (oClient.getClientID().equals("")){
            psClientID = "";
            psClientNm = "";
            psCltAddrs = "";
        }else{
            psClientID = oClient.getClientID();
            psClientNm = oClient.getClientNm();
            psCltAddrs = oClient.getCltAddrs();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
