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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

            ImageView point = new ImageView();

            switch(province.getProvinceType()){
                case COASTAL:
                    point.setImage(new Image("Point coastal.png"));
                    //createUnit(unitType.Fleet, province, province.getX(), province.getY());
                    break;
                case LAND:
                    point.setImage(new Image("Point.png"));
                   // createUnit(unitType.Army, province, province.getX(), province.getY());
                    break;
            }



            point.setX(province.getX());
            point.setY(province.getY());
            point.setTranslateX(50);
            point.setTranslateY(50);
            point.setScaleX(0.2);
            point.setScaleY(0.2);
            point.setTranslateX(-20);
            point.setTranslateY(-20);

            point.setOpacity(0.6);

            point.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    ImageView point = (ImageView) event.getTarget();

                    point.setOpacity(1);
                }
            });

            points.getChildren().add(point);

        }

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

    public void initProvinces() {
        //^([a-z]{3})\s'([^']+)'
        //Province $1 = new Province\("$2", "$1"\);

        //(Province )([a-z]{3})([^;]+;\r\n);
        //$1$2$3\t\tprovinces.add\($2\);\r\n


        //normal to code
        //\t([a-z]{3})\s'([^']+)'\s([0-9]+)\s([0-9]+);
        //\tProvince $1 = new Province\("$2", "$1"\, $3, $4\);\r\n\t\tprovinces.add\($1\);

        //star to setSupplycenter
        //\t\*\s([a-z]{3});
        //\t($1).setIsSupplyCenter\(true\);

        //----GERMANY//----
        Province kie = new Province("Kiel", "kie", 580, 540);
        provinces.add(kie);
        Province ber = new Province("Berlin", "ber", 675, 535);
        provinces.add(ber);
        Province pru = new Province("Prussia", "pru", 738, 505);
        provinces.add(pru);
        Province ruh = new Province("Ruhr", "ruh", 540, 601);
        provinces.add(ruh);
        Province mun = new Province("Munich", "mun", 603, 604);
        provinces.add(mun);
        Province sil = new Province("silesia", "sil", 715, 550);
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

        Province pic = new Province("Picardy", "pic", 434, 565);
        provinces.add(pic);
        Province bre = new Province("Brest", "bre", 335, 627);
        provinces.add(bre);
        Province par = new Province("Paris", "par", 403, 649);
        provinces.add(par);
        Province bur = new Province("Burgundy", "bur", 483, 624);
        provinces.add(bur);
        Province gas = new Province("Gascony", "gas", 346, 722);
        provinces.add(gas);
        Province mar = new Province("Marseilles", "mar", 473, 735);
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
        Province pie = new Province("piedmont", "pie", 550, 721);
        provinces.add(pie);
        Province ven = new Province("Venice", "ven", 624, 734);
        provinces.add(ven);
        Province tus = new Province("Tuscany", "tus", 607, 776);
        provinces.add(tus);
        Province rom = new Province("Rome", "rom", 650, 811);
        provinces.add(rom);
        Province apu = new Province("Apulia", "apu", 748, 829);
        provinces.add(apu);
        Province nap = new Province("Naples", "nap", 731, 864);
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
        Province boh = new Province("Bohemia", "boh", 698, 625);
        provinces.add(boh);
        Province gal = new Province("Galicia", "gal", 907, 618);
        provinces.add(gal);
        Province tyr = new Province("Tyrolia", "tyr", 667, 650);
        provinces.add(tyr);
        Province vie = new Province("Vienna", "vie", 748, 689);
        provinces.add(vie);
        Province bud = new Province("Budapest", "bud", 846, 691);
        provinces.add(bud);
        Province tri = new Province("Trieste", "tri", 707, 715);
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
        Province rum = new Province("Rumania", "rum", 944, 739);
        provinces.add(rum);
        Province ser = new Province("Serbia", "ser", 870, 805);
        provinces.add(ser);
        Province alb = new Province("Albania", "alb", 838, 840);
        provinces.add(alb);
        Province bul = new Province("Bulgaria", "bul", 941, 799);
        provinces.add(bul);
        Province gre = new Province("Greece", "gre", 879, 845);
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
        Province con = new Province("Constantinople", "con", 1030, 844);
        provinces.add(con);
        Province smy = new Province("Smyrna", "smy", 1071, 880);
        provinces.add(smy);
        Province ank = new Province("Ankara", "ank", 1175, 818);
        provinces.add(ank);
        Province arm = new Province("Armenia", "arm", 1339, 827);
        provinces.add(arm);
        Province syr = new Province("Syria", "syr", 1342, 945);
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
        Province cly = new Province("Clyde", "cly", 339, 400);
        provinces.add(cly);
        Province edi = new Province("Edinburgh", "edi", 372, 409);
        provinces.add(edi);
        Province lvp = new Province("Liverpool", "lvp", 359, 474);
        provinces.add(lvp);
        Province yor = new Province("York", "yor", 389, 501);
        provinces.add(yor);
        Province wal = new Province("Wales", "wal", 319, 525);
        provinces.add(wal);
        Province lon = new Province("London", "lon", 393, 537);
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


        //------RUSLAND------

        Province war = new Province("Warsaw", "war", 857, 552);
        provinces.add(war);
        Province lvn = new Province("Livonia", "lvn", 937, 430);
        provinces.add(lvn);
        Province urk = new Province("Urkaine", "urk", 1060, 590);
        provinces.add(urk);
        Province mos = new Province("Moscow", "mos", 1244, 412);
        Province stp = new Province("St. Petersburg", "stp", 1048, 344);
        provinces.add(mos);
        provinces.add(stp);
        Province fin = new Province("Finland", "fin", 883, 290);
        provinces.add(fin);
        Province sev = new Province("Sevastopol", "sev", 1384, 637);
        provinces.add(sev);

        war.setIsSupplyCenter(true);
        mos.setIsSupplyCenter(true);
        sev.setIsSupplyCenter(true);
        stp.setIsSupplyCenter(true);

        war.addBorder(lvn);
        war.addBorder(mos);
        war.addBorder(urk);
        lvn.addBorder(stp);
        lvn.addBorder(mos);
        urk.addBorder(mos);
        urk.addBorder(sev);
        mos.addBorder(stp);
        mos.addBorder(sev);
        stp.addBorder(fin);



        //-----------spain & africa-------

        Province spa = new Province("Spain", "spa", 204, 848);
        provinces.add(spa);
        Province por = new Province("Portugal", "por", 92, 827);
        provinces.add(por);
        Province naf = new Province("N. Africa", "naf", 200, 965);
        provinces.add(naf);
        Province tun = new Province("Tunis", "tun", 563, 944);
        provinces.add(tun);

        tun.setIsSupplyCenter(true);
        spa.setIsSupplyCenter(true);
        por.setIsSupplyCenter(true);

        spa.addBorder(por);
        naf.addBorder(tun);


        //-----NORWAY, SWEDEN DENMARK------

        Province nwy = new Province("Norway", "nwy", 615, 338);
        provinces.add(nwy);
        Province swe = new Province("Sweden", "swe", 710, 373);
        provinces.add(swe);
        Province den = new Province("Denmark", "den", 628, 418);
        provinces.add(den);

        den.setIsSupplyCenter(true);
        nwy.setIsSupplyCenter(true);
        swe.setIsSupplyCenter(true);

        nwy.addBorder(swe);


        //----independent provinces------

        Province hol = new Province("Holland", "hol", 507, 516);
        provinces.add(hol);
        Province bel = new Province("Belgium", "bel", 491, 553);
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

        //BORDERS FRANCE -> spain
        gas.addBorder(spa);
        gas.addBorder(spa);

        //BORDERS RUSSIA TURKEY, BULGARIA, AUSTRALIA FRANCE, SWEDEN
        sev.addBorder(arm);
        sev.addBorder(rum);
        urk.addBorder(rum);
        urk.addBorder(gal);
        war.addBorder(gal);
        war.addBorder(sil);
        war.addBorder(pru);
        fin.addBorder(swe);
        fin.addBorder(nwy);

        //OVERIGE
        den.addBorder(kie);


        // -----SEA'S------
        Province bar = new Province("Barents Sea", "bar", 1004, 50, Province.ProvinceType.COASTAL);
        provinces.add(bar);
        Province nrg = new Province("NRG Norwegian Sea", "nrg", 524, 186, Province.ProvinceType.COASTAL);
        provinces.add(nrg);
        Province nth = new Province("NTH North Sea", "nth", 481, 446, Province.ProvinceType.COASTAL);
        provinces.add(nth);
        Province ska = new Province("SKA", "ska", 650, 400, Province.ProvinceType.COASTAL);
        provinces.add(ska);
        Province bal = new Province("Baltic Sea", "bal", 798, 450, Province.ProvinceType.COASTAL);
        provinces.add(bal);
        Province bot = new Province("BOT Gulf of Botnia", "bot", 803, 312, Province.ProvinceType.COASTAL);
        provinces.add(bot);
        Province hel = new Province("HEL Helgoland Bight", "hel", 539, 484, Province.ProvinceType.COASTAL);
        provinces.add(hel);
        Province eng = new Province("English channel", "eng", 350, 555, Province.ProvinceType.COASTAL);
        provinces.add(eng);
        Province iri = new Province("Irish Sea", "iri", 260, 520, Province.ProvinceType.COASTAL);
        provinces.add(iri);
        Province nat = new Province("N. Atlantic", "nat", 135, 343, Province.ProvinceType.COASTAL);
        provinces.add(nat);
        Province mid = new Province("Mid Atlantic", "mid", 99, 655, Province.ProvinceType.COASTAL);
        provinces.add(mid);
        Province wes = new Province("West Mediterranean", "wes", 414, 870, Province.ProvinceType.COASTAL);
        provinces.add(wes);
        Province gol = new Province("GOL Golf of Legon", "gol", 466, 833, Province.ProvinceType.COASTAL);
        provinces.add(gol);
        Province tyn = new Province("Tyrhennian Sea", "tyn", 611, 895, Province.ProvinceType.COASTAL);
        provinces.add(tyn);
        Province ion = new Province("Ionian Sea", "ion", 789, 937, Province.ProvinceType.COASTAL);
        provinces.add(ion);
        Province adr = new Province("Adriantic Sea", "adr", 760, 804, Province.ProvinceType.COASTAL);
        provinces.add(adr);
        Province aeg = new Province("Aegean Sea", "aeg", 973, 933, Province.ProvinceType.COASTAL);
        provinces.add(aeg);
        Province eas = new Province("East Mid", "eas", 1148, 950, Province.ProvinceType.COASTAL);
        provinces.add(eas);
        Province bla = new Province("Black Sea", "bla", 1127, 763, Province.ProvinceType.COASTAL);
        provinces.add(bla);

        bar.addBorder(stp);
        bar.addBorder(swe);
        bar.addBorder(nrg);
        nrg.addBorder(nwy);
        nrg.addBorder(nth);
        nrg.addBorder(edi);
        nrg.addBorder(cly);
        nrg.addBorder(nat);
        nth.addBorder(nwy);
        nth.addBorder(edi);
        nth.addBorder(yor);
        nth.addBorder(lon);
        nth.addBorder(bel);
        nth.addBorder(hol);
        nth.addBorder(den);
        nth.addBorder(hel);
        nth.addBorder(ska);
        nth.addBorder(eng);
        ska.addBorder(den);
        ska.addBorder(nwy);
        ska.addBorder(swe);
        ska.addBorder(bal);
        bal.addBorder(kie);
        bal.addBorder(den);
        bal.addBorder(ber);
        bal.addBorder(pru);
        bal.addBorder(lvn);
        bal.addBorder(swe);
        bal.addBorder(bot);
        bot.addBorder(lvn);
        bot.addBorder(stp);
        bot.addBorder(fin);
        bot.addBorder(swe);
        hel.addBorder(den);
        hel.addBorder(kie);
        hel.addBorder(hol);
        eng.addBorder(bel);
        eng.addBorder(pic);
        eng.addBorder(bre);
        eng.addBorder(mid);
        eng.addBorder(iri);
        eng.addBorder(wal);
        eng.addBorder(lon);
        iri.addBorder(mid);
        iri.addBorder(nat);
        iri.addBorder(lvp);
        iri.addBorder(wal);
        nat.addBorder(cly);
        nat.addBorder(lvp);
        nat.addBorder(mid);
        mid.addBorder(bre);
        mid.addBorder(gas);
        mid.addBorder(spa);
        mid.addBorder(por);
        mid.addBorder(naf);
        mid.addBorder(wes);
        wes.addBorder(naf);
        wes.addBorder(tun);
        wes.addBorder(tyn);
        wes.addBorder(gol);
        wes.addBorder(spa);
        gol.addBorder(spa);
        gol.addBorder(mar);
        gol.addBorder(pie);
        gol.addBorder(tus);
        gol.addBorder(tyn);
        tyn.addBorder(tus);
        tyn.addBorder(rom);
        tyn.addBorder(nap);
        tyn.addBorder(ion);
        tyn.addBorder(tun);
        ion.addBorder(nap);
        ion.addBorder(apu);
        ion.addBorder(adr);
        ion.addBorder(alb);
        ion.addBorder(gre);
        ion.addBorder(aeg);
        ion.addBorder(tun);
        adr.addBorder(ven);
        adr.addBorder(tri);
        adr.addBorder(alb);
        adr.addBorder(apu);
        aeg.addBorder(gre);
        aeg.addBorder(bul);
        aeg.addBorder(con);
        aeg.addBorder(bla);
        aeg.addBorder(smy);
        aeg.addBorder(eas);
        eas.addBorder(smy);
        eas.addBorder(syr);
        bla.addBorder(con);
        bla.addBorder(bul);
        bla.addBorder(rum);
        bla.addBorder(sev);
        bla.addBorder(arm);
        bla.addBorder(ank);

    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

}
