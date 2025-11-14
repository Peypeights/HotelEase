# **HotelEase**  
### *A JavaFX-Based Hotel Management & Booking System*  
Course: **CSS123P / Computer Programming 3**

---

## ⭐ **Overview**

**HotelEase** is a hotel management and booking system built using **Java**, **JavaFX**, and **Maven**.  
It features a dual-role environment where:

- **Guests** can register, log in, and select among three hotel experiences:
  - 🌊 **Coastal Bliss**  
  - 🏞️ **Highland Haven**  
  - 🌆 **Urban Escape**

- **Admins** have access to management tools such as:
  - 📘 Managing bookings  
  - 🏨 Managing hotels  
  - 🚪 Managing rooms  

All data is stored **locally using CSV files** — no SQL or external database required.

---

## 🚀 **Features**

### **Guest**
- User registration & login  
- View available hotels  
- Choose between Coastal Bliss, Highland Haven, and Urban Escape  
- View available rooms  
- Make bookings  

### **Admin**
- Admin login  
- Dashboard overview  
- Manage hotel data  
- Manage room listings  
- Manage bookings  

### **System**
- Local CSV storage (no SQL)  
- Simple and intuitive JavaFX interface  
- Maven-based project structure  

---

## 🗃️ **Technologies Used**

- **Java** (tested on version **23.0.2**)  
- **JavaFX**  
- **Maven** (version **3.9.11**)  
- **CSV-based persistence** using built-in Java file handling  

---

## 📦 **Installation & Setup**

### **Prerequisites**
Make sure the following are installed:

- Java (preferably **23.0.2**, but compatible versions may work)
- Apache Maven (**3.9.11**)
- JavaFX SDK

### **Clone the Repository**
```bash
git clone https://github.com/yourusername/HotelEase.git
cd HotelEase
```
### **Clone the Repository**
```bash
mvn clean install
mvn javafx:run
```

## 🧩 **Project Structure**
```bash
Hotelease/
│
├── 📦 Source Packages
│ └── 📁 com.group7.hotelease
│ │   ├── 📄 App.java
│ │   └── 📄 SystemInfo.java
│ │
│ ├── 📁 Controllers
│ │   ├── 📄 AdminDashboardController.java
│ │   ├── 📄 HotelSelectionController.java
│ │   ├── 📄 LoginController.java
│ │   ├── 📄 RegistrationController.java
│ │   └── 📄 RoomListController.java
│ │
│ ├── 📁 Data
│ │   ├── 📄 bookings.csv
│ │   ├── 📄 hotels.csv
│ │   ├── 📄 rooms.csv
│ │   └── 📄 users.csv
│ │
│ ├── 📁 Models
│ │   ├── 📄 Booking.java
│ │   ├── 📄 Hotel.java
│ │   ├── 📄 Room.java
│ │   └── 📄 User.java
│ │
│ └── 📁 Utils
│   ├── 📄 CSVManager.java
│   └── 📄 SceneManager.java
│
└── 📂 Other Sources
  └── 📁 src/main/resources
    └── 📁 fxml
      ├── 📄 adminDashboard.fxml
      ├── 📄 hotelSelection.fxml
      ├── 📄 login.fxml
      ├── 📄 registration.fxml
      └── 📄 roomList.fxml
```

---

## 🎨 **Screenshots**
### **Login Menu**
![Login Screen](https://github.com/Peypeights/HotelEase/blob/10c98a4377d0290cccbca4479df49988ed21dbcd/Sample%20Pictures/Login%20Menu.jpg)
### **Registration Page**
![Registration Page](https://github.com/Peypeights/HotelEase/blob/10c98a4377d0290cccbca4479df49988ed21dbcd/Sample%20Pictures/Registration.jpg)
### **Admin Dashboard**
![Admin Dashboard](https://github.com/Peypeights/HotelEase/blob/63db77e91b6f81480c4e80e97cb34e7898eaf093/Sample%20Pictures/Admin%20Dashboard.jpg)
### **Hotel Selection (Coastal Bliss / Highland Haven / Urban Escape)**
![Hotel Selection](https://github.com/Peypeights/HotelEase/blob/10c98a4377d0290cccbca4479df49988ed21dbcd/Sample%20Pictures/Hotel%20Selection.jpg)
### **List of Available Rooms**
![List of Available Rooms](https://github.com/Peypeights/HotelEase/blob/10c98a4377d0290cccbca4479df49988ed21dbcd/Sample%20Pictures/Room%20Listing.jpg)

---

## 🧑‍💻 **Task Distribution**
### **Frontend**
- John Doe
- Pay Gorn

### **Backend**
- Throat Goat
- Lucky T Tiko

### 👥 **Team Members**
- Morish Alfonso R. Macayan: **Logic flow**
- Dwayne Anton C. Labao: 
- Yahj Abel R. Lapid: **GUI**
- Miguel Yñigo D. Sunga: 
