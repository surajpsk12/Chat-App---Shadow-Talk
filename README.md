
# ⚡ Shadow Talk - Anonymous Chat App

> **A modern, fast, and anonymous chat app built with MVVM architecture, Firebase, and Data Binding. Styled with a futuristic neon-dark theme inspired by Anonymous and tech culture.**

---
App Download Link : https://drive.google.com/file/d/1WhnEhMniPws0W_Qge1rcAhWP2a6FCiRc/view?usp=sharing


## 🚀 Features

- 🔐 Anonymous Firebase Authentication
- 🟢 Real-time Group Messaging (Firebase Realtime DB)
- 🧠 MVVM Architecture with LiveData & ViewModel
- 📦 Data Binding Integration for UI Components
- ✨ Neon Dark Theme (Anonymous / Tech Vibe)
- 💬 Create & Join Chat Groups
- 📨 Send & Receive Messages with Timestamp
- 📱 Fully Responsive & Animated UI

---

## 🎨 Tech Stack

- **Language:** Java
- **Architecture:** MVVM
- **UI:** XML + Data Binding
- **Backend:** Firebase (Realtime Database + Auth)
- **Design:** Neon + Dark Theme (`#121212`, `#00FFD1`, `#8B5CF6`)

---

## 🏗️ Project Structure

```

com.shadowtalk/
├── adapter/
│ ├── ChatAdapter.java
│ └── GroupAdapter.java
├── model/
│ ├── ChatMessage.java
│ └── ChatGroup.java
├── view/
│ ├── LoginActivity.java
│ ├── GroupActivity.java
│ └── ChatActivity.java
├── viewmodel/
│ └── MyViewModel.java
├── repository/
│ └── Repository.java
├── res/
│ ├── layout/
│ ├── drawable/
│ └── values/
└── AndroidManifest.xml



````

---


## ⚙️ Installation & Run

1. **Clone this repo:**

```bash
git clone https://github.com/surajpsk12/shadow-talk.git
cd shadow-talk
````

2. **Open in Android Studio.**

3. **Connect your Firebase project:**

   * Enable **Anonymous Authentication**
   * Setup **Firebase Realtime Database** (test mode or with rules)

4. **Run the app on an emulator or physical device.**

---

## 🔐 Firebase Rules (Realtime Database)

```json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
```

---


## 🧪 Future Enhancements

* ✅ Direct Messaging (DMs)
* ✅ Message Read Receipts
* ✅ User Online Status
* ✅ Emoji / Media Sharing
* ✅ Voice Messages
* ✅ End-to-End Encryption

---

## 🤝 Contribution

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change or improve.

---

## 📜 License

MIT © 2025 [Suraj Kumar](https://github.com/surajpsk12)

## 🌐 Connect With Me

* 🔗 [LinkedIn - Suraj Kumar](https://www.linkedin.com/in/surajvansh12/)
* 💻 [GitHub - surajpsk12](https://github.com/surajpsk12)




