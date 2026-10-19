# Proyecto: Ecosistema de Control de Acceso Híbrido (RFID + App Android)

## 1. Problemática
El presente proyecto busca modernizar el control de acceso físico (cerraduras/cortinas) reemplazando las llaves tradicionales por una solución híbrida. Los usuarios pueden ingresar usando tarjetas RFID o mediante una aplicación móvil conectada por Wi-Fi, garantizando el registro auditable y la seguridad de los datos según la norma ISO 27400.

## 2. Herramientas Utilizadas
* **Desarrollo Móvil:** Android Studio + Kotlin (Material Design).
* **Persistencia de Datos:** SharedPreferences (almacenamiento local seguro).
* **Hardware:** Arduino UNO R4 WiFi (Access Point y servidor web HTTP), Lector RFID MFRC522 y Arduino Esclavo (servomotor).
* **Conectividad:** Wi-Fi (Protocolo HTTP en puerto 80 y TCP en puerto 8080).

## 3. Funcionalidades
* Login y Registro de usuarios.
* Cifrado de contraseñas con Hash SHA-256.
* Autenticación de doble factor (Login + PIN de 4 dígitos).
* Panel de control con indicador visual de estado (candado rojo/verde).
* Historial de eventos con fecha y hora.
* Validación de tarjetas RFID físicas mediante el Arduino Maestro.

## 4. Seguridad (ISO 27400)
Se implementó el cifrado de credenciales (SHA-256) para proteger las contraseñas, un PIN de seguridad como segundo factor de autenticación y la validación de identidad física mediante tarjetas RFID, cumpliendo con los estándares de protección de secretos y control de acceso en entornos IoT/OT.

