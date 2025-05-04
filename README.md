# 🧩 How to Install BaseX and Run Your First XQuery

First of all, BaseX is kind of trash so I feel bad if you have to work with it hahaha, but I’ve been in your shoes before, so here’s some info to make it work in a simple way.

---

## 1️⃣ Install BaseX on Windows

### ✅ Step 1: Download BaseX

1. Go to [https://basex.org/download](https://basex.org/download)
2. Download the **ZIP** file or the **EXE installer** for Windows  
   *(I recommend the EXE if you want the quickest way)*

### ✅ Step 2: Install or run it

- If you downloaded the `.EXE`, install it as usual and open **BaseX GUI** from the start menu.
- If you downloaded the `.ZIP`, extract it and run `basexgui.bat`.

---

## 2️⃣ Create an XML Database

### ✅ Step 3: Prepare an XML file

Create a file called `books.xml` with the following content:

```xml
<library>
  <book>
    <title>1984</title>
    <author>George Orwell</author>
  </book>
  <book>
    <title>Fahrenheit 451</title>
    <author>Ray Bradbury</author>
  </book>
</library>
