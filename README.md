# TikTok TV (wrapper no oficial)

Proyecto Android Studio que envuelve la versión web de TikTok en una app
navegable con el control remoto de Google TV. **No es la app oficial de
TikTok** ni usa ninguna API privada: simplemente carga tiktok.com dentro
de un WebView, igual que lo haría el navegador Chrome de tu TV.

## Qué hace
- Carga https://www.tiktok.com/ en pantalla completa, horizontal.
- Flecha ↓ / ↑ del control: avanza o retrocede en el feed (scroll de un video).
- Botón OK / Enter: pausa o reanuda el video.
- Back: retrocede en el historial de navegación web.

## Limitaciones conocidas
- El login puede requerir verificación por SMS o captcha, como en cualquier navegador de escritorio.
- No hay algoritmo "For You" personalizado más allá de lo que TikTok sirve a un usuario logueado en web.
- Comentarios y mensajes directos son incómodos sin teclado (recomendado: control con teclado Bluetooth o app de teclado remoto en el móvil).
- TikTok puede cambiar su HTML/CSS en cualquier momento y romper el ajuste visual (el CSS inyectado en `MainActivity.kt` es lo primero a revisar si algo se ve mal).

## Cómo compilar el APK

1. Instala [Android Studio](https://developer.android.com/studio) (gratis).
2. Abre este proyecto: `File > Open` y selecciona la carpeta `TikTokTV`.
3. Deja que Gradle sincronice (la primera vez descarga dependencias, tarda unos minutos).
4. Ve a `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
5. El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Cómo instalarlo en tu Google TV

Opción A — con cable/red, usando ADB:
```
adb connect <IP_DE_TU_TV>:5555
adb install app/build/outputs/apk/debug/app-debug.apk
```
(Antes debes activar "Depuración USB por red" en Ajustes > Sistema > Información del dispositivo en tu Google TV — pulsa 7 veces sobre "Compilación" para activar Opciones de desarrollador.)

Opción B — sin cable:
1. Sube el APK a tu Google Drive o a un servidor propio.
2. Instala la app "Downloader" desde la Play Store del Google TV.
3. Pon el link de descarga del APK y sigue el instalador.

## Próximos pasos sugeridos
- Agregar un buscador simple con teclado en pantalla para escribir usuarios/hashtags.
- Guardar cookies de sesión de forma persistente (ya debería funcionar con `domStorageEnabled`, pero conviene probar tras cerrar y reabrir la app).
- Mejorar el CSS inyectado si TikTok cambia su estructura de página.
