## Descripción

Aplicación que estoy desarrollando para facilitar el manejo de archivos de guardado de videojuegos, permitiendo la **coexistencia de múltiples partidas** o "Perfiles" dentro de un mismo juego y guardando un **historial de checkpoints** para poder volver a un estado previo.


## Módulos
Originalmente, el programa iba a tener un módulo "**Servidor**" que se iba a encargar de manejar la base de datos con las partidas guardadas, y un módulo "**Cliente**" que va a permitir conectarse al módulo servidor local para cargar y descargar archivos de guardado según indicado por el usuario, o conectarse con módulos Servidor externos para obtener archivos de guardado de otros nodos, facilitando la transferencia de archivos entre computadoras para facilitar la continuidad al jugar una misma partida a través de distintos dispositivos. La idea era que se pueda mantener una instancia de "servidor" iniciada permanentemente en una computadora para poder "pedir" los archivos.

Tras repensarlo, decidi que sea un módulo unico que deba iniciarse en ambos nodos al momento de realizar la transferencia. Se puede hallar una version semi-funcional del modelo anterior en la version 0.1.0, commit **302e74e**
