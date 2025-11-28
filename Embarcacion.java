package Proyecto;
//clase que modela una embarcación del sistema EcoIsla Viajes.
//cada embarcación tiene 3 viajes diarios y cada viaje se representa con una matriz 5x5 de asientos (0=disponible, 1=reservado donde se verá adelante).
// Nombres: Jaime Salazar Martinez, Bruno Villaroel, José Menares, Yhonfreit Ascanio - ULAGOS
public class Embarcacion {
    private int id;
    private String nombre;

    private int[][] asientosMatutino;
    private int[][] asientosMediodia;
    private int[][] asientosVespertino;

    public static final int FILAS = 5; //lo dejamos como final porque así nos evitamos de colocar 5 en todos lados
    public static final int COLUMNAS = 5; //lo mismo.

    public Embarcacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;

        asientosMatutino = new int[FILAS][COLUMNAS];
        asientosMediodia = new int[FILAS][COLUMNAS];
        asientosVespertino = new int[FILAS][COLUMNAS];

        inicializarAsientos(asientosMatutino);
        inicializarAsientos(asientosMediodia);
        inicializarAsientos(asientosVespertino);
    }

    private void inicializarAsientos(int[][] matriz){
        for (int i = 0; i < FILAS; i++){
            for (int j = 0; j < COLUMNAS; j++){
                matriz[i][j] = 0; // 0 = disponible
            }
        }
    }

    public boolean asientoDisponible(int[][] matriz, int fila, int columna) {
        return matriz[fila][columna] == 0;
    }

    public void reservarAsiento(int[][] matriz, int fila, int columna) {
        if (asientoDisponible(matriz, fila, columna)) {
            matriz[fila][columna] = 1; // 1= ya reservado
        } 
        else{
            System.out.println("El asiento ya está ocupado.");
        }
    }

 
    public void mostrarMatriz(int[][] matriz, String nombreHorario) {
        System.out.println("Embarcación: " + nombre + " | Horario: " + nombreHorario);
        System.out.println("0 = disponible | 1 = reservado");
        System.out.println();

      
        System.out.print("   ");
        for (int col = 1; col <= COLUMNAS; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        char letraFila = 'A';
        for (int i = 0; i < FILAS; i++) {
            System.out.print(letraFila + "  ");
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
            letraFila++;
        }
        System.out.println();
    }

    
    public int[][] getAsientosMatutino(){
        return asientosMatutino;
    }
    
    public int[][] getAsientosMediodia(){
        return asientosMediodia;
    }
    
    public int[][] getAsientosVespertino(){ 
        return asientosVespertino;
    }
 public String getNombre(){return nombre;
 }
}
