# Recocido Simulado Aplicado a VRP
Universidad Nacional Autónoma de México, Facultad de Ciencias.

Proyecto de Tesis para obtener el Título de Licenciado en Ciencias de la Computación.

Presenta Jael Alejandro Estudillo Carranza.

Bajo dirección del Dr. Canek Peláez Valdés

El codigo necesario para probar la implementación se encuentra dentro de la carpeta VehicleRoutingProblem.
Dentro de la carpeta base se encuentra la base de datos con la información necesaria para realizar el calculo de distancias y tiempo obtenidos, en caso de que 
quiera consultarse su información se debe realziar mediante sqlite.

El archivo de configuración info.properties permite configurar los valores con los que trabajara el sistema, cada campo resulta evidente en que aspecto controla, el campo mode indica la variante del problema a ejecutar, entre las 3 definidas en el documento.

El archivo pedidos.txt, nos permite definir la lista de ciudades por Id, al igual que la cantidad de paquetes que corresponden, y de igual manera al principio del 
archivo nos permite indicar el Id del deposito inicial

Dentro de la carpeta configurations viene ejemplos de configuraciones que se utilizarón tanto para el conjunto de 20 ciudades como para el de 30 al igual que los archivos de pedidos que se utilziarón para cada configuración, y de igual forma ejemplos para el archivo de pedidos, usado con cada grupo.

Para poder correr el programa se le deben pasar los siguientes argumentos

 - -v n -> Esto nos permite indicarle que version se va a ejecutar 1 (CVRP) o 2 (VRPTW)
 - -p n -> Esto nos permite indicarle con cuantos threads va a trabajar
 - -d date -> Esto nos permite indicarle el datetime que tomara como fecha de inicio (en cado de VRTPW)
 - -r path -> Nos permite indicarle la ruta global de la semilla que queramos verificar

Para ejecutar CVRP con 3 threads se pasan como parametros

 -v 1 -p 3

Para ejecutar VRPTW con 1 Thread y fecha 12/05/2010-16:05:40

 -v 2 -p 1 -d 12/05/2010-16:05:40 

En caso de querer validar un resultado de la ejecucion anterior seria

 -v 2 -p 1 -d 12/05/2010-16:05:40 -r /home/user/Documentos/VehicleRoutingProblem/report/report-TW-1p-1m-20540/solution1323/Seed-1323.txt

Cuando se realiza una ejecucion, se creara la carpeta report, donde segun al configuracion indicada se creara una carpeta donde se guardaran todas las posibles 
soluciones con todos los documentos necesarios para analizar y replicar el resultado particular.
