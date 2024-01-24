# TheBills---Introduction-to-mobile-systems

---

# TheBills App Overview

TheBills is a mobile application designed to facilitate shared expenses and financial management among users within a shared space, often referred to as a "room." The app provides a user-friendly interface for users to log in, register, and interact within different rooms, each serving as a collaborative space for managing shared bills and expenses.

## Purpose
TheBills app aims to streamline shared financial management among groups of users, making it easy for individuals in shared spaces, such as households or travel groups, to collaboratively manage and track expenses. The intuitive design and well-organized layout contribute to an efficient and user-friendly experience.

---

# Funcionalities:

## Room Package Overview

The `room` package in TheBills app is responsible for managing rooms, which are collaborative spaces where users can interact and manage shared expenses. This package consists of several classes and interfaces that handle various functionalities related to rooms.

### 1. RoomManager Class (`RoomManager.java`)

#### Key Responsibilities:
- **Firebase Interaction**: Manages interactions with Firebase Realtime Database to handle room-related operations.
- **User Authentication**: Handles user authentication using Firebase Auth.
- **Room Creation and Joining**: Provides methods for creating new rooms, joining existing rooms, and adding users to rooms.
- **Data Retrieval**: Retrieves data such as room names and user rooms from the database.

#### Notable Methods:
- `createNewRoom(String roomName)`: Initiates the process of creating a new room.
- `joinRoom(String desiredRoomKey)`: Attempts to join an existing room.
- `getUserRooms(GetUserRoomsCallback callback)`: Retrieves rooms associated with the current user.

### 2. RoomManagerCreateRoom Class (`RoomManagerCreateRoom.java`)

#### Key Responsibilities:
- **Dialog Creation**: Extends `AppCompatDialogFragment` to create a dialog for creating a new room.
- **User Input Handling**: Captures user input for room name and triggers the room creation process.

#### Notable Methods:
- `onCreateDialog(Bundle savedInstanceState)`: Overrides to create the dialog.

### 3. RoomManagerJoinRoom Class (`RoomManagerJoinRoom.java`)

#### Key Responsibilities:
- **Dialog Creation**: Extends `AppCompatDialogFragment` to create a dialog for joining an existing room.
- **User Input Handling**: Captures user input for the desired room key and triggers the room joining process.

#### Notable Methods:
- `onCreateDialog(Bundle savedInstanceState)`: Overrides to create the dialog.

### 4. RoomManagerRecycleViewAdapter Class (`RoomManagerRecycleViewAdapter.java`)

#### Key Responsibilities:
- **RecyclerView Adapter**: Extends `RecyclerView.Adapter` to handle the display of rooms in a RecyclerView.
- **Item Click Handling**: Defines the behavior when a room item in the RecyclerView is clicked.

#### Notable Methods:
- `onCreateViewHolder(ViewGroup parent, int viewType)`: Creates RecyclerView items.
- `onBindViewHolder(MyViewHolder holder, int position)`: Binds data to RecyclerView items.
- `getItemCount()`: Returns the number of rooms.

### 5. RoomRecycleViewEvent Interface (`RoomRecycleViewEvent.java`)

#### Key Responsibilities:
- **Event Handling**: Defines a callback interface for handling item clicks in the RecyclerView.

### 6. RoomTuple Class (`RoomTuple.java`)

#### Key Responsibilities:
- **Data Model**: Represents the structure of a room, including its ID, name, owner ID, creation date, and associated users.
- **User Management**: Manages the addition of users to a room.

#### Notable Methods:
- `addUser(String user)`: Adds a user to the room.

---

## Bill Package Overview

The `bill` package in TheBills app is responsible for managing bills, including their creation, display, and interaction. This package consists of several classes and interfaces that handle various functionalities related to bills.

### 1. Bill Class (`Bill.java`)

#### Key Responsibilities:
- **Bill Data Display**: Manages the display of bill details, including name, owner, date, and cost breakdown.
- **Firebase Interaction**: Interacts with Firebase Realtime Database to retrieve bill data.
- **Bill Deletion**: Provides functionality to delete a bill from the database.
- **User Interface Handling**: Manages user interface components such as TextViews, RecyclerView, and ProgressBar.
- **Intent Handling**: Retrieves data from the Intent passed to the activity.

#### Notable Methods:
- `getBillDataFromDatabase(String billId)`: Retrieves bill data from the database.
- `convertMapToTimestamp(Map<String, Object> createDateMap)`: Converts a map to a timestamp string.
- `moveAfterDeleting()`: Navigates to the Room activity after deleting a bill.

### 2. BillsView Class (`BillsView.java`)

#### Key Responsibilities:
- **Bills Display**: Displays a list of bills for a specific room.
- **Firebase Interaction**: Retrieves bill data from Firebase Realtime Database.
- **User Interface Handling**: Manages RecyclerView, ProgressBar, and Add Bill button.
- **Intent Handling**: Retrieves data from the Intent passed to the activity.
- **Navigation**: Navigates to the CreateBill activity when the Add Bill button is clicked.

#### Notable Methods:
- `refreshData()`: Refreshes the data displayed in the RecyclerView.

### 3. BillDataRecycleViewAdapter Class (`BillDataRecycleViewAdapter.java`)

#### Key Responsibilities:
- **RecyclerView Adapter**: Manages the display of user-specific costs in a RecyclerView.
- **Item Display**: Displays user names and their corresponding costs.

### 4. BillManager Class (`BillManager.java`)

#### Key Responsibilities:
- **Firebase Interaction**: Manages interactions with Firebase Realtime Database for bill-related operations.
- **User Authentication**: Handles user authentication using Firebase Auth.
- **Bill Creation**: Provides methods for creating new bills and updating associated user and room data.
- **Data Retrieval**: Retrieves bill data from the database.

#### Notable Methods:
- `addBill(String roomKey, Map<String, Float> localCostMap, Timestamp createDate, float totalCost, String billName, String billOwner)`: Adds a new bill to the database.
- `getRoomBills(String roomKey, GetRoomBillsCallback callback)`: Retrieves bills associated with a specific room.
- `getBillData(String billId, GetBillDataCallback callback)`: Retrieves detailed data for a specific bill.

### 5. BillManagerRecycleViewAdapter Class (`BillManagerRecycleViewAdapter.java`)

#### Key Responsibilities:
- **RecyclerView Adapter**: Manages the display of bills in a RecyclerView.
- **Item Click Handling**: Defines behavior when a bill item in the RecyclerView is clicked.

### 6. BillRecycleViewEvent Interface (`BillRecycleViewEvent.java`)

#### Key Responsibilities:
- **Event Handling**: Defines a callback interface for handling item clicks in the RecyclerView.

### 7. CostRecycleViewAdapter Class (`CostRecycleViewAdapter.java`)

#### Key Responsibilities:
- **RecyclerView Adapter**: Manages the display of user-specific costs in a RecyclerView during bill creation.
- **Cost Change Handling**: Notifies listeners when user-specific costs are changed.

#### Notable Methods:
- `setLocalCostsMapAutoCalc(Float totalCost)`: Automatically calculates and sets costs based on the total cost.
- `refreshAdapter()`: Refreshes the RecyclerView adapter.

---

# Remover (`Remover` class)

The `Remover` class is responsible for handling the removal of data (bills, rooms, and users) from the Firebase Realtime Database. Here's an overview of its main functionalities:

## Firebase Database Initialization

Initializes the Firebase components, such as authentication, the current user, and database references, in the constructor.

## Removing Bill

- `removeBillFromRoom(String roomId, String billId)`: Removes a bill from a specific room by deleting the corresponding value in the "bills" child node.
- `removeBillFromUser(String userId, String billId)`: Removes a bill associated with a user by deleting the corresponding value in the "bills" child node under the user's reference.
- `removeBill(String billId)`: Removes a bill from the main "bills" node.

## Removing Room

- `removeRoomData(String roomId)`: Removes the data of a specific room by deleting the corresponding value in the "rooms" node.
- `removeRoom(String roomId, ArrayList<String> users, ArrayList<String> bills)`: Removes a room and associated bills and user references. Iterates through the list of bills and users to perform the removal.

## Leaving Room

- `leaveRoom(String roomId, String userId)`: Removes a user from a room by deleting the corresponding value in the "rooms" child node under the user's reference and the "users" child node under the room's reference.

---

# Results-related Classes

## BillTuple class

Represents a tuple containing the owner and the cost map of a bill.
Provides a `toString()` method for generating a human-readable string representation.

## Results Manager (`ResultsManager` class)

### Attributes:

- `billManager`: An instance of `BillManager` for fetching bill data.
- `billList`: A list to store `BillTuple` objects.
- `resultList`: A list to store `ResultTuple` objects.
- `uniqueOwners`: A set to store unique owners from bills.
- `billsReceivedCount`: A counter to track the number of bills received.

### Methods:

- `getBillsForRoom(String roomKey)`: Retrieves bills for a specific room and processes the data asynchronously.
- `performOperationAfterBillsReceived()`: Performs operations after receiving all bills, including logic processing and setting the results in the provided `TextView`.

### Logic Processing:

- `getOwners()`: Logs unique owners from the bills.
- `getBills()`: Logs the size and details of the bill list.
- `logic()`: Processes the logic to calculate user total costs based on bills.
- `getAllToString()`: Generates a formatted string containing results for display.

## ResultTuple class

Represents a tuple containing the owner and their total costs based on bills.
Provides a `toString()` method for generating a human-readable string representation.

---

## Login.xml (Login Screen)

- **textHeader**: Header displaying "Login".
- **textFieldEmail**: Input field for entering email address.
- **textFieldPassword**: Input field for entering password.
- **buttonLogin**: Button to submit the login process.
- **textRegister**: Text encouraging the creation of a new account.

## MainActivity.xml (Main Screen)

- **textViewEmail**: Displays the user's email address.
- **buttonLogout**: Button for logging out.
- **buttonCreateRoom**: Button for creating a new room.
- **buttonJoinRoom**: Button for joining an existing room.
- **textWelcome**: Displays a welcome message.
- **roomRecycleView**: Displays a list of rooms.
- **progressBarRooms**: Progress bar while loading the list of rooms.
- **buttonRefresh**: Button to refresh the list of rooms.

## Register.xml (Registration Screen)

- **textHeader**: Header displaying "Register".
- **textFieldEmail**: Input field for entering email address.
- **textFieldPassword**: Input field for entering password.
- **buttonRegister**: Button to submit the registration process.
- **textRegister**: Text encouraging login if an account already exists.

## Room.xml (Room Screen)

- **RoomName**: Displays the name of the room.
- **buttonLeaveRoom**: Button for leaving the room.
- **buttonShowBills**: Button for displaying bills in the room.
- **textViewResult**: Displays a summary or message in the room.

## EditTextRoomName.xml (Room Name Input)

- **editTextRoomName**: Input field for entering the room name.

## EditTextRoomKey.xml (Room Key Input)

- **editTextRoomKey**: Input field for entering the room key.

## CardViewRecycleViewBills.xml (Card View for Bills in RecyclerView)

- Displays information about a bill, such as the bill name and ID.

## CardViewRecycleViewBillData.xml (Card View for Bill Data in RecyclerView)

- Displays data related to a bill, such as user and cost.

## CardViewRecycleViewCosts.xml (Card View for User Costs in RecyclerView)

- Displays information about user costs in the room.

## CardViewRecycleViewRooms.xml (Card View for Rooms in RecyclerView)

- Displays information about a room, such as the room name and key.
