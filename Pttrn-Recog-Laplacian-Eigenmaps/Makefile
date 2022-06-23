Project_Laplacian_Eigenmaps: main.o scattering_transform.o laplacian_eigenmaps.o
	gcc -o Project_Laplacian_Eigenmaps -I/usr/local/include -L/usr/local/lib -lgsl -lgslcblas main.o scattering_transform.o laplacian_eigenmaps.o

main.o: main.c main.h scattering_transform.h laplacian_eigenmaps.h
	gcc -c -I/usr/local/include -L/usr/local/lib -lgsl -lgslcblas main.c

scattering_transform.o: scattering_transform.c main.h scattering_transform.h laplacian_eigenmaps.h
	gcc -c -I/usr/local/include -L/usr/local/lib -lgsl -lgslcblas scattering_transform.c

laplacian_eigenmaps.o: laplacian_eigenmaps.c main.h scattering_transform.h laplacian_eigenmaps.h
	gcc -c -I/usr/local/include -L/usr/local/lib -lgsl -lgslcblas laplacian_eigenmaps.c