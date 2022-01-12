module com.pelr.socialnetwork_extins {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.pelr.socialnetwork_extins to javafx.fxml;
    exports com.pelr.socialnetwork_extins;
    exports com.pelr.socialnetwork_extins.controllers;
    exports com.pelr.socialnetwork_extins.controls to javafx.fxml;
    opens com.pelr.socialnetwork_extins.controllers to javafx.fxml;
}