# 🧩 Cómo instalar BaseX y hacer tu primera consulta XQuery

[BaseX](https://basex.org) es una base de datos nativa para XML muy útil para trabajar con XQuery. Aquí te explico cómo instalarlo y empezar a usarlo.

---

## 1️⃣ Instalar BaseX en Windows

### ✅ Paso 1: Descargar BaseX

1. Ve a [https://basex.org/download](https://basex.org/download)
2. Descarga el archivo **ZIP** o el instalador **EXE** para Windows  
   *(Recomiendo el EXE si quieres algo rápido)*

### ✅ Paso 2: Instalar o ejecutar

- Si descargaste el `.EXE`, instálalo normalmente y abre **BaseX GUI** desde el menú de inicio.
- Si descargaste el `.ZIP`, extrae y abre `basexgui.bat`.

---

## 2️⃣ Crear una base de datos XML

### ✅ Paso 3: Tener un archivo XML

Crea un archivo llamado `libros.xml` con este contenido:

```xml
<biblioteca>
  <libro>
    <titulo>1984</titulo>
    <autor>George Orwell</autor>
  </libro>
  <libro>
    <titulo>Fahrenheit 451</titulo>
    <autor>Ray Bradbury</autor>
  </libro>
</biblioteca>
