module com.group7.hotelease {
    requires javafx.controls;
    requires javafx.fxml;
    
    // Make all automatic modules available
    requires static com.opencsv;
    
    opens com.group7.hotelease to javafx.fxml;
    opens com.group7.hotelease.Controllers to javafx.fxml;
    
    exports com.group7.hotelease;
    exports com.group7.hotelease.Controllers;
}