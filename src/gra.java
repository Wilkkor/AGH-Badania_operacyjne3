import java.util.Scanner;

public class gra {
    int plansza[][]=new int[50][50];
    int glebokosc_minimax=6;

    void wyswietl(){
        System.out.printf("  ");
        for (int i=0;i<50;i++){
            System.out.printf("%2d",i);
        }
        System.out.println();
        for(int i=0;i<50;i++){
            System.out.printf("%2d",i);
            for (int j=0;j<50;j++){
                System.out.printf(" %c",plansza[i][j]);
            }
            System.out.println();
        }
    }

    int [][] kopia_planszy(int [][] plansza_teraz){
        int tab[][]=new int[50][50];
        for (int i=0;i<50;i++){
            for (int j=0;j<50;j++){
                tab[i][j]=plansza_teraz[i][j];
            }
        }
        return tab;
    }

    void decyzja(){
        boolean r;
        int punkt[]=new int[2];
        int [][]kopia_planszy=kopia_planszy(plansza);
        int max=0,wartosc;
        for (int i=0;i<50;i++){
            for (int j=0;j<50;j++){
                r=false;
                r = sprawdz_otoczenie(r, i, j);
                if(r){
                    kopia_planszy[i][j]='x';////////////////////////////////nie tak, mie na tej samej strukturze, tu wątki ???
                    wartosc=minimax(kopia_planszy,glebokosc_minimax,true);
                    if(wartosc>max){
                        max=wartosc;
                        punkt[0]=i;
                        punkt[1]=j;
                    }
                }
            }
        }
        plansza[punkt[0]][punkt[1]]='x';
    }

    int minimax(int [][]plansza_teraz,int glebokosc,boolean krzyzyk){
        if(glebokosc>0){
            int max=0,wartosc;
            int [][]plansza_kopia=kopia_planszy(plansza_teraz);
            boolean r;
            for (int i=0;i<50;i++){
                for (int j=0;j<50;j++){
                    r=false;
                    r = sprawdz_otoczenie(r, i, j);
                    if(r){
                        plansza[i][j]=krzyzyk?'x':'o';
                        wartosc=minimax(plansza_kopia,glebokosc-1,!krzyzyk);// start wątków
                        if(wartosc>max){
                            max=wartosc;
                        }
                    }
                }
            }
            return max;
        }
        else{
            int miara=0;
            //tu policz miare
            return miara;
        }
    }

    private boolean sprawdz_otoczenie(boolean r, int i, int j) {
        if(plansza[i][j]==' '){
            for (int k=-1;k<2;k++){
                for (int l=-1;l<2;l++){
                    if(plansza[i+k][j+l]!=' '){
                        r=true;
                        break;
                    }
                }
                if(r){
                    break;
                }
            }
        }
        return r;
    }

    public static void main(String[] args) {
        int x,y;
        Scanner in=new Scanner(System.in);
        gra kolkoikrzyzyk=new gra();
        for(int i=0;i<50;i++){
            for (int j=0;j<50;j++){
                kolkoikrzyzyk.plansza[i][j]=' ';
            }
        }
        kolkoikrzyzyk.wyswietl();
        while(true){
            System.out.println("podaj wolne pole na które chcesz postawić o ");
            x=in.nextInt();
            y=in.nextInt();
            kolkoikrzyzyk.plansza[x][y]='o';
            kolkoikrzyzyk.decyzja();
        }
    }
}
