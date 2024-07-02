
module FXML {
    requires javafx.controls;
    requires javafx.fxml;


    opens FXML to javafx.fxml;
    exports FXML;
}