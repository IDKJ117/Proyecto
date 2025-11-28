public class Embarcacion {
    private String nombre;
    private int[][] matutino;
    private int[][] mediodia;
    private int[][] vespertino;

    public Embarcacion(String nombre) {
        this.nombre = nombre;
        matutino = new int[5][5];
        mediodia = new int[5][5];
        vespertino = new int[5][5];
    }

    public String getNombre() {
        return nombre;
    }

    public int[][] getMatutino() {
        return matutino;
    }

    public int[][] getMediodia() {
        return mediodia;
    }

    public int[][] getVespertino() {
        return vespertino;
    }

    public boolean disponible(int[][] m, int f, int c) {
        return m[f][c] == 0;
    }

    public void reservar(int[][] m, int f, int c) {
        m[f][c] = 1;
    }
}
