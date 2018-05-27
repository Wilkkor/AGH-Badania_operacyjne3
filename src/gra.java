import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class gra{
    static  int wymiar=30;
    static BlockingQueue<Punkt_z_wartoscia> results = new ArrayBlockingQueue<>(wymiar *wymiar );

    private int plansza[]=new int[wymiar*wymiar];
    private int glebokosc_minimax=3;
    static char moj_znak='x';
    static char znak_przeciwnika='o';

    static class MinMax extends Thread{
        int [] plansza_teraz;
        int glebokosc;
        boolean czy_moj_ruch;
        int x,y;
        MinMax(int [] kopia_planszy,int glebokosc_minimax,boolean czy_moj_ruch,int x,int y){
            plansza_teraz=kopia_planszy;
            glebokosc=glebokosc_minimax;
            this.czy_moj_ruch=czy_moj_ruch;
            this.x=x;
            this.y=y;
        }
        public void run(){
            try {
                Punkt_z_wartoscia punkt=new Punkt_z_wartoscia(minimax(plansza_teraz,glebokosc,czy_moj_ruch),x,y);
                results.put(punkt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    }

    private void wyswietl(){
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

    private int decyzja() throws InterruptedException {
        boolean r;
        List <MinMax> threads=new ArrayList<MinMax>();
        //Punkt punkt=new Punkt();
        int [] kopia_planszy;
        //int max=-2147483648;
        Punkt_z_wartoscia punkt_z_wartoscia,max_z_wartoscia=new Punkt_z_wartoscia(-2147483648,-1,-1);
        for (int i=0;i<wymiar;i++){
            for (int j=0;j<wymiar;j++){
                //r=false;
                r = sprawdz_otoczenie(plansza,false, i, j);
                if(r){
                    kopia_planszy=plansza.clone();
                    kopia_planszy[i*wymiar+j]=moj_znak;////////////////////////////////nie tak, mie na tej samej strukturze, tu wątki ??? oraz tylko po liście znanych elementów
                    threads.add(new MinMax(kopia_planszy,glebokosc_minimax,false,i,j));
//                    wartosc=minimax(kopia_planszy,glebokosc_minimax,false);
//                    if(wartosc>max){
//                        max=wartosc;
//                        punkt.x=i;
//                        punkt.y=j;
//                        System.out.printf("nadalem punktowi %d %d \n",i,j);
//                    }
                }
            }
        }
        for(int j=0;j<threads.size();j++){
            threads.get(j).start();
        }
        for(int j=0;j<threads.size();j++){
            punkt_z_wartoscia=results.take();
            if(max_z_wartoscia.wartosc<punkt_z_wartoscia.wartosc){
                max_z_wartoscia=punkt_z_wartoscia;
            }
        }
        System.out.printf("Postawilem na %d %d \n",max_z_wartoscia.x,max_z_wartoscia.y);
        plansza[max_z_wartoscia.x*wymiar+max_z_wartoscia.y]=moj_znak;
        return max_z_wartoscia.wartosc;
    }

    private static int ocen_planszę(int [] plansza_teraz,boolean czy_moj_ruch) {
        int miara=0;
        int il_moich=0,il_przeciwnika=0;
        boolean czy_bylo_puste=false;
        int jii=0,i_0=0,j_0=0;
        // xxx max xx 2 oooo min ooo -2 oo -1
        for (int przejscie=0;przejscie<6;przejscie++){
            switch (przejscie){
                case 0:i_0=0;j_0=0;break;
                case 1:i_0=0;j_0=0;break;
                case 2:i_0=0;j_0=0;break;
                case 3:i_0=0;j_0=0;break;
                case 4:i_0=0;j_0=0;break;
                case 5:i_0=wymiar-1;j_0=0;break;
            }
            boolean r=true;
            while (i_0<wymiar&&j_0<wymiar&&i_0>=0&&j_0>=0){
                int i=i_0;
                int j=j_0;
                while(i<wymiar&&j<wymiar&&i>=0&&j>=0){
                    if(il_przeciwnika==5){
                        return -2147483647;
                    }
                    else if (il_moich==5){
                        miara+=20;
                    }
                    else if(plansza_teraz[i*wymiar+j]==' '&&czy_bylo_puste&&(il_moich>1||il_przeciwnika>1)){
                        if(il_moich>0){
                            if(czy_moj_ruch){
                                if(il_moich==2){
                                    miara+=2;
                                }
                                else if(il_moich==3){
                                    miara+=4;
                                }
                                else if (il_moich>3){
                                    miara+=10;
                                }
                            }
                            else {
                                if(il_moich==2){
                                    miara+=1;
                                }
                                else if(il_moich==3){
                                    miara+=2;
                                }
                                else if (il_moich>3){
                                    miara+=5;
                                }
                            }
                        }
                        else {
                            if(czy_moj_ruch){
                                if(il_przeciwnika==2){
                                    miara-=2;
                                }
                                else if(il_przeciwnika==3){
                                    miara-=20;
                                }
                                else if (il_przeciwnika>3){
                                    return -2147483647;
                                }
                            }
                            else {
                                if(il_przeciwnika==2){
                                    miara-=5;
                                }
                                if(il_przeciwnika>=3){
                                    return -2147483647;
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
                    switch (przejscie){
                        case 0:i++;break;
                        case 1:j++;break;
                        case 2:i++;j++;break;
                        case 3:i++;j++;break;
                        case 4:i--;j++;break;
                        case 5:i--;j++;break;
                    }
                }
                switch (przejscie){
                    case 0:j_0++;break;
                    case 1:i_0++;break;
                    case 2:j_0++;break;
                    case 3:i_0++;break;
                    case 4:i_0++;break;
                    case 5:j_0++;break;
                }
                il_moich=0;
                il_przeciwnika=0;
                czy_bylo_puste=false;
            }
        }

        return miara;
    }

    private static boolean sprawdz_otoczenie(int [] plansza_teraz,boolean r, int i, int j) {// to nie bedzie chyba potrzebne
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

    public static void main(String[] args) throws InterruptedException {
        int x,y;
        Scanner in=new Scanner(System.in);
        gra kolkoikrzyzyk=new gra();
        for(int i=0;i<gra.wymiar;i++){
            for (int j=0;j<gra.wymiar;j++){
                kolkoikrzyzyk.plansza[i*gra.wymiar+j]=' ';
            }
        }
        while(true){
            kolkoikrzyzyk.wyswietl();
            do {
                System.out.println("podaj wolne pole na które chcesz postawić o ");
                x = in.nextInt();
                y = in.nextInt();
            }while (kolkoikrzyzyk.plansza[x*gra.wymiar+y]!=' ');
            kolkoikrzyzyk.plansza[x*gra.wymiar+y]=gra.znak_przeciwnika;
            if(gra.ocen_planszę(kolkoikrzyzyk.plansza,true)==-2147483647)
            {
                System.out.println("wygrales");
                return;
            }
            //kolkoikrzyzyk.wyswietl();
            if(kolkoikrzyzyk.decyzja()==-2147483647){
                System.out.println("Ech, wygrales, musiałem coś przeoczyć, ale następnym razem cię ogram !!!");
                return;
            }
            int il_moich=0;
            int jii=0,i_0=0,j_0=0;
            if(sprawdz_czy_nie_wygralem(kolkoikrzyzyk, il_moich, jii, i_0, j_0)){
                kolkoikrzyzyk.wyswietl();
                System.out.println("wygralem uchachacha");
                return;
            }
        }
    }

    private static boolean sprawdz_czy_nie_wygralem(gra kolkoikrzyzyk, int il_moich, int jii, int i_0, int j_0) {
        for (int przejscie=0;przejscie<6;przejscie++){
            switch (przejscie){
                case 0:i_0=0;j_0=0;break;
                case 1:i_0=0;j_0=0;break;
                case 2:i_0=0;j_0=0;break;
                case 3:i_0=0;j_0=0;break;
                case 4:i_0=0;j_0=0;break;
                case 5:i_0=wymiar-1;j_0=0;break;
            }
            boolean r=true;
            while (i_0<wymiar&&j_0<wymiar&&i_0>=0&&j_0>=0){
                int i=i_0;
                int j=j_0;
                while(i<wymiar&&j<wymiar&&i>=0&&j>=0){
                    if (il_moich==5){
                        return true;
                    }
                    else if(kolkoikrzyzyk.plansza[i*wymiar+j]==znak_przeciwnika)il_moich=0;
                    else if(kolkoikrzyzyk.plansza[i*wymiar+j]==moj_znak)il_moich++;
                    switch (przejscie){
                        case 0:i++;break;
                        case 1:j++;break;
                        case 2:i++;j++;break;
                        case 3:i++;j++;break;
                        case 4:i--;j++;break;
                        case 5:i--;j++;break;
                    }
                }
                switch (przejscie){
                    case 0:j_0++;break;
                    case 1:i_0++;break;
                    case 2:j_0++;break;
                    case 3:i_0++;break;
                    case 4:i_0++;break;
                    case 5:j_0++;break;
                }
                il_moich=0;
            }
        }
        return false;
    }

}