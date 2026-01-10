
# 📚 SmartStudyApp

SmartStudyApp is an Android-based smart study application designed to enhance digital reading and note-taking for students.
It provides an interactive PDF and image reader with highlighting, bookmarking, and contextual study assistance features.

---

## 🚀 Features

### 📖 Document Reading

* Open and read **PDF documents**
* Open and view **image files** (PNG, JPG)
* Smooth zoom and pan using gesture controls

### ✨ Highlighting

* Highlight selected text areas on PDF pages
* Highlights are **saved per document and page**
* Undo last highlight on a page
* Highlights persist when navigating between pages

### 🔖 Bookmarks

* Bookmark specific pages in a document
* Automatically restores last bookmarked or last viewed page

### 📝 Notes

* Add and manage notes linked to document pages
* Notes are organized page-wise

### 🌙 Smart Context Features

* Automatic **Night Mode** based on ambient light
* Stretch reminders during long reading sessions
* Focus-aware behavior using device sensors

---

## 🛠️ Technology Stack

| Layer           | Technology                    |
| --------------- | ----------------------------- |
| Platform        | Android (Java)                |
| UI              | XML Layouts                   |
| PDF Rendering   | `PdfRenderer`                 |
| Image Viewing   | `PhotoView`                   |
| Database        | Room (SQLite)                 |
| Architecture    | Activity-based modular design |
| Version Control | Git & GitHub                  |

---

## 📂 Supported File Types

| File Type                | Status                               |
| ------------------------ | ------------------------------------ |
| PDF                      | ✅ Supported                          |
| Images (PNG, JPG)        | ✅ Supported                          |
| Word (.doc, .docx)       | ❌ Not supported (external apps only) |
| PowerPoint (.ppt, .pptx) | ❌ Not supported (external apps only) |

> Note: Word and PowerPoint files are intentionally not rendered inside the app.

---

## 🧭 How It Works

1. User selects a document using the **Open File** option
2. App detects file type:

   * PDF → Opens with reader, highlights, bookmarks enabled
   * Image → Opens in image viewer mode
3. Highlights and bookmarks are stored locally using Room database
4. Last reading position is restored automatically

---

## 📸 Screens (Optional)
### Home Screen
![20260110_233152](https://github.com/user-attachments/assets/64c1bfaf-b91f-4cda-999e-2aa9ac2c8525)

### PDF Viewer
![20260110_233204](https://github.com/user-attachments/assets/7d30eb50-77a6-4b55-afe6-27fe381ed4e2)

### Text Highlighting
![20260110_233216](https://github.com/user-attachments/assets/4f8f60c6-507e-44fd-9697-e6fd476d6b60)

### Bookmarks & Notes
![20260110_233235](https://github.com/user-attachments/assets/37cd1384-c338-4012-8a40-c2b238d3bb78)

### Image Viewer Support
![20260110_233249](https://github.com/user-attachments/assets/4e81c77a-3ef0-40f8-9cba-b2df4823452e)

---

## 🔐 Permissions Used

* `READ_EXTERNAL_STORAGE` – Access selected documents
* `ACCESS_FINE_LOCATION` – Context-based study features
* Sensor access – Ambient light, proximity, and motion detection

---

## 🧪 Project Status

* ✔ Core features completed
* ✔ Stable reading & highlighting
* ✔ Git workflow with `dev` and `main` branches
* 🔄 Future enhancements planned

---

## 👨‍💻 Developers

**Name:** *[Archchika, Rahitha, Thilukshika, Thushanthy, Cavinaya, Thanusa]*
**Project Type:** Academic Project
**Repository:** SmartStudyApp

---

## 📄 License

This project is developed for **educational purposes**.
All rights reserved.


