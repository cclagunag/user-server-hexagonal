# **CRUD de usuarios con arquitectura hexagonal**


# 1. Requisitos del programa

- versión del java : 17
- instalar intellij IDEA
- instalar JDK Java 17
- instalar el administrador de paquetes maven versión 3.9.9
- instalar postgresSQL

# 2. Para nuestra preferencia se utilizo el IDE intellij IDEA, importar el proyecto

# 3. Configurar base de datos postgresSQL

- Ir al **application.properties** y cambiar el usuario y la contraseña de acuerdo a la configuración de su base de datos postgresSQL

```properties
spring.datasource.username=postgres
spring.datasource.password=123456
```

# 4. Compilar la aplicación desde la clase llamada UserApplication

![img.png](img.png)

# 5. Para abrir la documentación **Swagger** digitar en el navegador la siguiente ruta

- http://localhost:8080/swagger-ui/index.html#/

![img_1.png](img_1.png)

# 6. Implementación de la arquitectura hexagonal

![img_2.png](img_2.png)

# 7. Ejecutar las pruebas unitarias

- Seleccionar el proyecto con click derecho y pulsar la opción **run 'All test'**

![img_3.png](img_3.png)

# 8. Generar el reporte JaCoCo

- ejecutar el siguiente comando: mvn clean verify

![img_4.png](img_4.png)

# 9. Cómo encontrar el reporte de JaCoCo

- ir a la carpeta target/site/index.html

![img_5.png](img_5.png)

# 10. Cómo crear un usuario

- para crear un usuario solamente pide el username y el password y devuelve toda la información creada
- **Nota: el username debe ser único de lo contrario lanza error**

![img_6.png](img_6.png)

# 11. Cómo editar un usuario

- para editar un usuario pide el id, username y password
- **Nota: el username debe ser único y el id del usuario debe existir de lo contrario lanza error**

![img_7.png](img_7.png)

# 11. Cómo traer usuario por id

- esta consulta pide obligatoriamente un usuario con id que existe en base de datos
- **Nota: el id del usuario debe existir de lo contrario lanza error**

![img_8.png](img_8.png)

# 12. Cómo traer un listado de usuarios

![img_9.png](img_9.png)

# 13. Cómo eliminar el usuario

- esta consulta pide obligatoriamente un usuario con id que existe en base de datos
- **Nota: el id del usuario debe existir de lo contrario lanza error**

![img_10.png](img_10.png)

- listar usuarios otra vez, ya no debe éxistir el usuario que se ha eliminado

![img_11.png](img_11.png)