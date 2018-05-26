import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class gra{
    int wymiar=30;
    //private static int wymiar_dla_blocking_queue;
    //static BlockingQueue<Integer> results = new ArrayBlockingQueue<Integer>(wymiar_dla_blocking_queue*wymiar_dla_blocking_queue);
    Random random=new Random();
    int plansza[]=new int[wymiar*wymiar];
    int glebokosc_minimax=2;
    char moj_znak='x';
    char znak_przeciwnika='o';

//    static class MinMax extends Thread{
//        int [] plansza_teraz;
//        int glebokosc;
//        boolean czy_moj_ruch;
//        MinMax(int [] kopia_planszy,int glebokosc_minimax,boolean czy_moj_ruch){
//            plansza_teraz=kopia_planszy;
//            glebokosc=glebokosc_minimax;
//            this.czy_moj_ruch=czy_moj_ruch;
//        }
//        public void run(){
//            try {
//                results.put(mean);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            //System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
//        }
//        int minimax(int [] plansza_teraz,int glebokosc,boolean czy_moj_ruch){
//            if(glebokosc>0){
//                int maxlubmin=czy_moj_ruch?-2147483648:2147483647,wartosc;
//                int []plansza_kopia;
//                boolean r;
//                for (int i=0;i<wymiar;i++){
//                    for (int j=0;j<wymiar;j++){
//                        r=false;
//                        r = sprawdz_otoczenie(plansza_teraz,r, i, j);
//                        if(r){
//                            plansza_kopia=plansza_teraz.clone();
//                            plansza_kopia[i*wymiar+j]=czy_moj_ruch?moj_znak:znak_przeciwnika;
//                            wartosc=minimax(plansza_kopia,glebokosc-1,!czy_moj_ruch);// start wątków
//                            if(czy_moj_ruch&&wartosc>maxlubmin){
//                                maxlubmin=wartosc;
//                            }
//                            if (!czy_moj_ruch&&wartosc<maxlubmin){
//                                maxlubmin=wartosc;
//                            }
//                        }
//                    }
//                }
//                return maxlubmin;
//            }
//            else{
//                return ocen_planszę(plansza_teraz,czy_moj_ruch);
//            }
//        }
//    }


    void wyswietl(){
        System.out.printf("  ");
        for (int i=0;i<wymiar;i++){
            System.out.printf("%2d|",i);
        }
        System.out.println();
        for(int i=0;i<wymiar;i++){
            System.out.printf("%2d",i);
            for (int j=0;j<wymiar;j++){
                System.out.printf(" %c|",plansza[i*wymiar+j]);
            }
            System.out.println();
        }
    }

    void decyzja(){
        boolean r;
        Punkt punkt=new Punkt();
        int [] kopia_planszy;
        int max=-2147483648,wartosc;
        for (int i=0;i<wymiar;i++){
            for (int j=0;j<wymiar;j++){
                r=false;
                r = sprawdz_otoczenie(plansza,r, i, j);
                if(r){
                    kopia_planszy=plansza.clone();
                    kopia_planszy[i*wymiar+j]=moj_znak;////////////////////////////////nie tak, mie na tej samej strukturze, tu wątki ??? oraz tylko po liście znanych elementów
                    wartosc=minimax(kopia_planszy,glebokosc_minimax,false);
                    if(wartosc>max){
                        max=wartosc;
                        punkt.x=i;
                        punkt.y=j;
                        System.out.printf("nadalem punktowi %d %d \n",i,j);
                    }
                }
            }
        }
        System.out.printf("Postawilem na %d %d \n",punkt.x,punkt.y);
        plansza[punkt.x*wymiar+punkt.y]=moj_znak;
    }

    int minimax(int [] plansza_teraz,int glebokosc,boolean czy_moj_ruch){
        if(glebokosc>0){
            int maxlubmin=czy_moj_ruch?-2147483648:2147483647,wartosc;
            int []plansza_kopia;
            boolean r;
            for (int i=0;i<wymiar;i++){
                for (int j=0;j<wymiar;j++){
                    r=false;
                    r = sprawdz_otoczenie(plansza_teraz,r, i, j);
                    if(r){
                        plansza_kopia=plansza_teraz.clone();
                        plansza_kopia[i*wymiar+j]=czy_moj_ruch?moj_znak:znak_przeciwnika;
                        wartosc=minimax(plansza_kopia,glebokosc-1,!czy_moj_ruch);// start wątków
                        if(czy_moj_ruch&&wartosc>maxlubmin){
                            maxlubmin=wartosc;
                        }
                        if (!czy_moj_ruch&&wartosc<maxlubmin){
                            maxlubmin=wartosc;
                        }
                    }
                }
            }
            return maxlubmin;
        }
        else{
            return ocen_planszę(plansza_teraz,czy_moj_ruch);
        }
    }

    private int ocen_planszę(int [] plansza_teraz,boolean czy_moj_ruch) {
        int miara=0;
        int il_moich=0,il_przeciwnika=0;
        boolean czy_bylo_puste=false;
        int jii=0,i_0=0,j_0=0;
        // xxx max xx 2 oooo min ooo -2 oo -1
        while(jii<2){
            int j=j_0;
            int i=i_0;
            int ktora_opcja=0;
            while(ktora_opcja<3){
                if(il_przeciwnika==5){
                    return -2147483648;
                }
                else if (il_moich==5){
                    return 2147483647;
                }
                else if(plansza_teraz[i*wymiar+j]==' '&&czy_bylo_puste&&(il_moich>1||il_przeciwnika>1)){
                    if(il_moich>0){
                        if(czy_moj_ruch){
                            if(il_moich==2){
                                miara+=2;
                            }
                            if(il_moich==3){
                                miara+=10;
                            }
                        }
                        else {
                            if(il_moich==2){
                                miara+=1;
                            }
                            if(il_moich==3){
                                miara+=2;
                            }
                        }
                    }
                    else {
                        if(czy_moj_ruch){
                            if(il_przeciwnika==2){
                                miara-=1;
                            }
                            if(il_przeciwnika==3){
                                miara-=2;
                            }
                        }
                        else {
                            if(il_przeciwnika==2){
                                miara-=3;
                            }
                            if(il_przeciwnika==3){
                                return -2147483648;
                            }
                        }
                    }
                    il_moich=0;
                    il_przeciwnika=0;
                    czy_bylo_puste=true;
                }
                else if(plansza_teraz[i*wymiar+j]==moj_znak&&il_przeciwnika>0){
                    czy_bylo_puste=false;
                    il_przeciwnika=0;
                    il_moich++;
                }
                else if(plansza_teraz[i*wymiar+j]==znak_przeciwnika&&il_moich>0){
                    czy_bylo_puste=false;
                    il_moich=0;
                    il_przeciwnika++;
                }
                else if(plansza_teraz[i*wymiar+j]==' '){
                    czy_bylo_puste=true;
                    il_moich=0;
                    il_przeciwnika=0;
                }
                else if(plansza_teraz[i*wymiar+j]==znak_przeciwnika)il_przeciwnika++;
                else if(plansza_teraz[i*wymiar+j]==moj_znak)il_moich++;
                switch (ktora_opcja){
                    case 0:{
                        if(jii==0){
                            j++;
                        }
                        else {
                            i++;
                        }
                        break;
                    }
                    case 1:{
                        j++;
                        i++;
                        break;
                    }
                    case 2:{
                        if(jii==0){
                            j++;
                            i--;
                        }
                        else{
                            j--;
                            i++;
                        }
                        break;
                    }
                }
                if(i==wymiar||j==wymiar||i==-1||j==-1){
                    ktora_opcja++;
                    i=i_0;j=j_0;
                    il_moich=0;
                    il_przeciwnika=0;
                    czy_bylo_puste=false;
                }
            }
            if(jii==0){
                if(i_0<wymiar-1){
                    i_0++;
                }else {
                    i_0=0;
                    jii++;
                }
            }
            else if(jii==1){
                if(j_0<wymiar-1){
                    j_0++;
                }
                else {
                    j_0=0;
                    jii++;
                }
            }
        }
        return miara;
    }

    private boolean sprawdz_otoczenie(int [] plansza_teraz,boolean r, int i, int j) {// to nie bedzie chyba potrzebne
        if(plansza_teraz[i*wymiar+j]==' '){
            for (int k=-1;k<2;k++){
                for (int l=-1;l<2;l++){
                    if(i+k>=0&&j+l>=0&&i+k<wymiar&&j+l<wymiar&&plansza_teraz[(i+k)*wymiar+j+l]!=' '){
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
        for(int i=0;i<kolkoikrzyzyk.wymiar;i++){
            for (int j=0;j<kolkoikrzyzyk.wymiar;j++){
                kolkoikrzyzyk.plansza[i*kolkoikrzyzyk.wymiar+j]=' ';
            }
        }
        while(true){
            kolkoikrzyzyk.wyswietl();
            do {
                System.out.println("podaj wolne pole na które chcesz postawić o ");
                x = in.nextInt();
                y = in.nextInt();
            }while (kolkoikrzyzyk.plansza[x*kolkoikrzyzyk.wymiar+y]!=' ');
            kolkoikrzyzyk.plansza[x*kolkoikrzyzyk.wymiar+y]=kolkoikrzyzyk.znak_przeciwnika;
            if(kolkoikrzyzyk.ocen_planszę(kolkoikrzyzyk.plansza,true)==-2147483648)
            {
                System.out.println("wygrales");
                return;
            }
            kolkoikrzyzyk.wyswietl();
            kolkoikrzyzyk.decyzja();
            if(kolkoikrzyzyk.ocen_planszę(kolkoikrzyzyk.plansza,false)==2147483647)
            {
                System.out.println("wygralem hahaha");
                return;
            }
        }
    }
}
