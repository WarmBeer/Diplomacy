package models;

import domains.Army;
import domains.Fleet;
import domains.Province;
import domains.Unit;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import static application.Main.GAME_VIEW;

public class GameModel implements Model {

    private enum unitType {
        Army,
        Fleet
    }

    Group root; //Kaart en UI render groep
    Group troops; //Troepen render groep
    Group points; //Provincie punt render groep
    ArrayList<Province> provinces = new ArrayList<Province>();

    public void initProvinces() {
        //^([a-z]{3})\s'([^']+)'
        //Province $1 = new Province\("$2", "$1"\);

        //(Province )([a-z]{3})([^;]+;\r\n)
        //$1$2$3\t\tprovinces.add\($2\);\r\n

        //----GERMANY//----
        Province kie = new Province("Kiel", "kie", 686, 814);
        provinces.add(kie);
        Province ber = new Province("Berlin", "ber", 793, 812);
        provinces.add(ber);
        Province pru = new Province("Prussia", "pru", 861, 782);
        provinces.add(pru);
        Province ruh = new Province("Ruhr", "ruh", 647, 899);
        provinces.add(ruh);
        Province mun = new Province("Munich", "mun", 715, 903);
        provinces.add(mun);
        Province sil = new Province("silesia", "sil", 841, 819);
        provinces.add(sil);

        kie.setIsSupplyCenter(true);
        ber.setIsSupplyCenter(true);
        mun.setIsSupplyCenter(true);

        kie.addBorder(ber);
        kie.addBorder(ruh);
        kie.addBorder(mun);
        ber.addBorder(pru);
        ber.addBorder(sil);
        ber.addBorder(mun);
        ruh.addBorder(mun);
        mun.addBorder(sil);
        sil.addBorder(pru);

        //----FRANCE//----

        Province pic = new Province("Picardy", "pic", 533, 849);
        provinces.add(pic);
        Province bre = new Province("Brest", "bre", 426, 935);
        provinces.add(bre);
        Province par = new Province("Paris", "par", 499, 966);
        provinces.add(par);
        Province bur = new Province("Burgundy", "bur", 586, 931);
        provinces.add(bur);
        Province gas = new Province("Gascony", "gas", 438, 1067);
        provinces.add(gas);
        Province mar = new Province("Marseilles", "mar", 575, 1085);
        provinces.add(mar);

        bre.setIsSupplyCenter(true);
        par.setIsSupplyCenter(true);
        mar.setIsSupplyCenter(true);

        pic.addBorder(bre);
        pic.addBorder(par);
        pic.addBorder(bur);
        bre.addBorder(par);
        bre.addBorder(gas);
        par.addBorder(gas);
        par.addBorder(bur);
        bur.addBorder(gas);
        bur.addBorder(mar);
        gas.addBorder(mar);


        //----ITALY//----
        Province pie = new Province("piedmont", "pie", 658, 1066);
        provinces.add(pie);
        Province ven = new Province("Venice", "ven", 738, 1083);
        provinces.add(ven);
        Province tus = new Province("Tuscany", "tus", 720, 1142);
        provinces.add(tus);
        Province rom = new Province("Rome", "rom", 766, 1190);
        provinces.add(rom);
        Province apu = new Province("Apulia", "apu", 872, 1216);
        provinces.add(apu);
        Province nap = new Province("Naples", "nap", 853, 1264);
        provinces.add(nap);

        ven.setIsSupplyCenter(true);
        rom.setIsSupplyCenter(true);
        nap.setIsSupplyCenter(true);

        pie.addBorder(ven);
        pie.addBorder(tus);
        ven.addBorder(tus);
        ven.addBorder(apu);
        ven.addBorder(rom);
        tus.addBorder(rom);
        rom.addBorder(apu);
        rom.addBorder(nap);
        nap.addBorder(apu);



        //----AUSTRIA (hungary?)//-----
        Province boh = new Province("Bohemia", "boh", 818, 932);
        provinces.add(boh);
        Province gal = new Province("Galicia", "gal", 1044, 922);
        provinces.add(gal);
        Province tyr = new Province("Tyrolia", "tyr", 784, 967);
        provinces.add(tyr);
        Province vie = new Province("Vienna", "vie", 872, 1021);
        provinces.add(vie);
        Province bud = new Province("Budapest", "bud", 978, 1024);
        provinces.add(bud);
        Province tri = new Province("Trieste", "tri", 827, 1057);
        provinces.add(tri);

        vie.setIsSupplyCenter(true);
        bud.setIsSupplyCenter(true);
        tri.setIsSupplyCenter(true);

        boh.addBorder(gal);
        boh.addBorder(vie);
        boh.addBorder(tyr);
        gal.addBorder(bud);
        gal.addBorder(vie);
        tyr.addBorder(vie);
        tyr.addBorder(tri);
        vie.addBorder(bud);
        vie.addBorder(tri);
        bud.addBorder(tri);


        //----bulgaria?//---//---
        Province rum = new Province("Rumania", "rum", 1083, 1083);
        provinces.add(rum);
        Province ser = new Province("Serbia", "ser", 966, 1176);
        provinces.add(ser);
        Province alb = new Province("Albania", "alb", 947, 1230);
        provinces.add(alb);
        Province bul = new Province("Bulgaria", "bul", 1080, 1168);
        provinces.add(bul);
        Province gre = new Province("Greece", "gre", 1020, 1317);
        provinces.add(gre);

        rum.setIsSupplyCenter(true);
        ser.setIsSupplyCenter(true);
        bul.setIsSupplyCenter(true);

        rum.addBorder(ser);
        rum.addBorder(bul);
        ser.addBorder(bul);
        ser.addBorder(alb);
        ser.addBorder(gre);
        alb.addBorder(gre);
        bul.addBorder(gre);


        //-----TURKEY//---//---
        Province con = new Province("Constantinople", "con", 1176, 1236);
        provinces.add(con);
        Province smy = new Province("Smyrna", "smy", 1221, 1286);
        provinces.add(smy);
        Province ank = new Province("Ankara", "ank", 1333, 1200);
        provinces.add(ank);
        Province arm = new Province("Armenia", "arm", 1510, 1212);
        provinces.add(arm);
        Province syr = new Province("Syria", "syr", 1513, 1377);
        provinces.add(syr);

        con.setIsSupplyCenter(true);
        smy.setIsSupplyCenter(true);
        ank.setIsSupplyCenter(true);

        con.addBorder(ank);
        con.addBorder(smy);
        smy.addBorder(ank);
        smy.addBorder(arm);
        smy.addBorder(syr);
        ank.addBorder(arm);
        arm.addBorder(syr);


        //-----ENGLANG//---//---//---//---
        Province cly = new Province("Clyde", "cly", 437, 632);
        provinces.add(cly);
        Province edi = new Province("Edinburgh", "edi", 466, 632);
        provinces.add(edi);
        Province lvp = new Province("Liverpool", "lvp", 452, 722);
        provinces.add(lvp);
        Province yor = new Province("York", "yor", 484, 760);
        provinces.add(yor);
        Province wal = new Province("Wales", "wal", 408, 798);
        provinces.add(wal);
        Province lon = new Province("London", "lon", 488, 810);
        provinces.add(lon);

        cly.addBorder(edi);
        cly.addBorder(lvp);
        edi.addBorder(lvp);
        edi.addBorder(yor);
        lvp.addBorder(yor);
        lvp.addBorder(wal);
        yor.addBorder(lon);
        yor.addBorder(wal);
        wal.addBorder(lon);


        //----independent provinces//---//---

        Province hol = new Province("Holland", "hol", 612, 780);
        provinces.add(hol);
        Province bel = new Province("Belgium", "bel", 594, 832);
        provinces.add(bel);

        //BORDERS GERMANY -> france and austria (hol & beg);

        kie.addBorder(hol);
        ruh.addBorder(bel);
        ruh.addBorder(bur);
        mun.addBorder(tyr);
        mun.addBorder(boh);
        sil.addBorder(boh);
        sil.addBorder(gal);

        //BORDERS FRANCE -> italy (hol & beg);
        mar.addBorder(pie);
        pic.addBorder(bel);
        bur.addBorder(bel);

        //BORDERS ITALY -> austria
        pie.addBorder(tyr);
        ven.addBorder(tyr);
        ven.addBorder(tri);

        //BORDERS AUSTRIA -> bulgaria?
        gal.addBorder(rum);
        bud.addBorder(rum);
        bud.addBorder(ser);
        tri.addBorder(ser);
        tri.addBorder(alb);

        //BORDERS BULGARIA? -> turkey
        bul.addBorder(con);
    }

    @FXML
    public void show(Stage stage) throws Exception{

        root = new Group();
        troops = new Group();
        points = new Group();

        Parent pane = FXMLLoader.load(
                getClass().getResource(GAME_VIEW));

        Scene scene = new Scene( root, 1920, 1080 );
        root.getChildren().addAll(pane, troops, points);

        stage.setScene(scene);
    }

    @FXML
    public void init() {
        initProvinces();

        //Maak troepen aan
        for(Province province: provinces){
            createUnit(unitType.Army, province, (double) province.getX(), (double) province.getY());
        }

//        createUnit(unitType.Army, 400d, 650d);
//        createUnit(unitType.Army, 480d, 740d);
//        createUnit(unitType.Army, 330d, 630d);
//        createUnit(unitType.Fleet, 10d, 650d);
    }

    public void createUnit(unitType unit, Province province, double x, double y) {
        Unit troop = null;

        System.out.println(x);

        switch (unit) {
            case Army:
                troop = new Army(province);
                break;
            case Fleet:
                troop = new Fleet(province);
                break;
        }

        province.addUnit(troop);

        moveUnit(troop, x, y);

        //Render troepen
        troops.getChildren().add(troop);

        troop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                Unit thisUnit = (Unit) event.getTarget();

                ArrayList<Province> borderProvinces = thisUnit.province.getBorders();
                System.out.println("clicked on province " + thisUnit.province.getName());
                for(Province province: borderProvinces){
                    System.out.println("Border province: "+province.getName());
                }
            }
        });

    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

}
